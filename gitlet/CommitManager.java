package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Repository.OBJECTS_DIR;
import static gitlet.Utils.*;

/**
 * TODO(QingZhiLiangCheng):Commit Manager, 管理commit的相关操作
 *
 * @author QingZhiLiangCheng
 */
public class CommitManager implements Serializable {
    private final File COMMITS_DIR;

    CommitManager() {
        COMMITS_DIR = join(OBJECTS_DIR, "commits");
    }


    /**
     * Done[Completed on 2025-05-20](QingZhiLiangCheng): 初始化
     */
    public void init() {
        COMMITS_DIR.mkdirs();
    }

    /**
     * Done(QingZhiLiangCheng)[Completed on 2025-05-11] 获取commit文件
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng): 重构 修改方法名 将方法从Repository.java移到该文件夹
     *
     * @param id commit id
     * @return commit文件
     */
    public Commit getCommit(String id) {
        File commitFile = join(COMMITS_DIR, id);
        return readObject(commitFile, Commit.class);
    }


    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): 保存commit
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng): 重构 移动位置 修改函数声明 增加一个参数
     * FIXME(QingZhiLiangCheng): 要不要构建动态哈希表 更快的找CommitID？
     * 将id保存至 `.gitlet/objects/commit/id`
     * {@link Utils#writeObject(File, Serializable)}
     */
    public void saveCommit(Commit commit) {
        File commitFile = new File(COMMITS_DIR, commit.getId());
        writeObject(commitFile, commit);
    }

    /**
     * Done(chenghsi)[Completed on 2025-05-21] :检测commit里面是否含有指定ID的文件
     */
    public boolean containsFile(String fileName) {
        File targetFilePath = join(COMMITS_DIR, fileName);
        return targetFilePath.exists();
    }

    /**
     * Done[Completed on 2025-05-21](ChengShi): find [commit message]
     * 打印所有包含指定提交信息的提交 ID，每行一个。如果有多个这样的提交，则将 ID 打印在不同的行上。
     */
    public void findCommitsByMessage(String message) {


        if (!COMMITS_DIR.exists() || !COMMITS_DIR.isDirectory()) {
            System.out.println("没有找到包含指定提交信息的提交 ID");
            return;
        }

        List<String> commitIds = plainFilenamesIn(COMMITS_DIR);
        boolean found = false;

        for (String commitId : commitIds) {
            Commit commit = getCommit(commitId);
            if (commit.getMessage().contains(message)) {
                System.out.println(commitId);
                found = true;
            }
        }

        if (!found) {
            System.out.println("没有找到包含指定提交信息的提交 ID");
        }
    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): 打印提交信息
     */
    private void printCommitLog(Commit commit) {
        List<String> parents = commit.getParents();
        System.out.println("===");
        System.out.println("commit " + commit.getId());
        if (parents.size() > 1) {
            System.out.print("Merge:");
            for (String id : parents) {
                System.out.print(" " + id.substring(0, 7));
            }
            System.out.print("\n");
        }
        System.out.println("Date: " + commit.getTimestamp().toString());
        System.out.println("Message: " + commit.getMessage());
        HashMap<String, String> commitBlobMap = commit.getBlobMap();
        if (!commitBlobMap.isEmpty()) {
            System.out.print("Files:");
            for (String filename : commitBlobMap.keySet()) {
                System.out.print(" " + filename);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    /**
     * Done[Completed on 2025-05-25](QingZhiLiangCheng): 寻找最近公共祖先
     * 广度优先搜索 + 双向遍历
     */
    public Commit getSplitCommit(Commit commitA, Commit commitB) {
        Deque<Commit> dequeCommitA = new ArrayDeque<>();
        Deque<Commit> dequeCommitB = new ArrayDeque<>();
        HashSet<String> visitedCommitA = new HashSet<>();
        HashSet<String> visitedCommitB = new HashSet<>();

        dequeCommitA.add(commitA);
        dequeCommitB.add(commitB);

        while (!dequeCommitA.isEmpty() || !dequeCommitB.isEmpty()) {
            if (!dequeCommitA.isEmpty()) {
                Commit currA = dequeCommitA.poll();
                if (visitedCommitB.contains(currA.getId())) {
                    return currA;
                }
                visitedCommitA.add(currA.getId());
                addParentsToDeque(currA, dequeCommitA);
            }

            if (!dequeCommitB.isEmpty()) {
                Commit currB = dequeCommitB.poll();
                if (visitedCommitA.contains(currB.getId())) {
                    return currB;
                }
                visitedCommitB.add(currB.getId());
                addParentsToDeque(currB, dequeCommitB);
            }
        }
        return null;
    }

    /**
     * Done[Completed on 2025-05-25](QingZhiLiangCheng)
     */
    private void addParentsToDeque(Commit commit, Deque<Commit> deque) {
        List<String> parent = commit.getParents();
        for (String parentId : parent) {
            deque.add(getCommit(parentId));
        }
    }
}

