package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static gitlet.Repository.OBJECTS_DIR;
import static gitlet.Utils.*;

/**
 * TODO(QingZhiLiangCheng):Commit Manager, 管理commit的相关操作
 *
 * @author QingZhiLiangCheng
 */
public class CommitManager implements Serializable{
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
}

