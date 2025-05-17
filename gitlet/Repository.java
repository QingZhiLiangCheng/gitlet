package gitlet;


import java.io.File;
import java.util.HashMap;
import java.util.List;

import static gitlet.Utils.*;
import static java.lang.System.exit;


/**
 * Represents a gitlet repository.
 * TODO(QingZhiLiangCheng): 搭建Repository class框架.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-10
 */
public class Repository {

    /**
     * TODO(QingZhiLiangCheng): add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     * <br>
     * 我的目录结构
     * .gitlet (folder)
     *      |── objects (folder) // 存储commit对象文件
     *          |-- commits
     *          |-- blobs
     *      |── refs (folder)
     *          |── heads (folder) //指向目前的branch
     *              |-- master (file)
     *              |-- other file      //表示其他分支的路径
     *          |-- HEAD (file)     // 保存HEAD指针的对应hashname
     *      |-- addstage (folder)       // 暂存区文件夹
     *      |-- removestage (folder)
     */

    /**
     * current working directory.
     */
    private static File CWD;
    /**
     * the .gitlet directory.
     */
    private static File GITLET_DIR;

    /**
     * the objects directory<br>
     * 包含commits 和 blogs
     */
    public static File OBJECTS_DIR;
    public static File COMMIT_DIR;
    public static File BLOBS_DIR;

    /**
     * the refs directory.<br>
     * 包含heads 和 HEAD<br>
     * - heads 存分支
     */
    public static File REFS_DIR;
    public static File HEADS_DIR;
    public static File HEAD_POINT;

    /**
     * the stage directory.
     */
    public static File ADD_STAGE_DIR;
    public static AddStage addStage;
    public static File REMOVE_STAGE_DIR;
    public static RemoveStage removeStage;

    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): 构造函数<br>
     * 创建CWD<br>
     * 然后调用 {@link #configDIRS()}
     */
    public Repository() {
        CWD = new File(System.getProperty("user.dir"));
        configDIRS();
        addStage = new AddStage();
        removeStage = new RemoveStage();
    }

    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): config directory
     * 配置好每个文件夹的位置
     */
    private void configDIRS() {
        GITLET_DIR = join(CWD, ".gitlet");
        OBJECTS_DIR = join(GITLET_DIR, "objects");
        COMMIT_DIR = join(OBJECTS_DIR, "commits");
        BLOBS_DIR = join(OBJECTS_DIR, "blobs");
        REFS_DIR = join(GITLET_DIR, "refs");
        HEADS_DIR = join(REFS_DIR, "heads");
        HEAD_POINT = join(REFS_DIR, "HEAD");
        ADD_STAGE_DIR = join(GITLET_DIR, "addstage");
        REMOVE_STAGE_DIR = join(GITLET_DIR, "removestage");
    }

    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): init<br>
     * 架构图在`READEME.md`中都画好了<br>
     * 如果存在.gitlet 视为错误 退出程序 打印错误信息<br>
     * "A Gitlet version-control system already exists in the current directory."<br>
     * 创建各个文件夹{@link #createInitDir()}<br>
     * 创建 Commit 0 {@link Commit#Commit()}<br>
     * 存储commit {@link Commit#save()}<br>
     * 创建master和HEAD {@link #initReference(String)} ()}<br>
     */
    public void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            exit(0);
        }
        createInitDir();
        Commit initCommit = new Commit();
        initCommit.save();
        initReference(initCommit.getId());
    }

    /**
     * Done[Completed on 2025-05-14](QingZhiLiangCheng) add command<br>
     * <p>
     * 1.文件名是空？ 抛异常{@link GitletException} "Please enter a file name."<br>
     * 2.工作目录中不存在此文件？<br>
     * 3. remove区中存在
     * 4. add区中存在
     * 5.如果文件已经被track 具体表现为与blobMap中的文件名一样<br>
     * 5.1 内容一致: 不需要纳入暂存区<br>
     * 5.2 内容不一致: 加入暂存区<br>
     * FIXME(QingZhiLiangCheng) 是否未来可以commit部分add
     * FIXME(QingZhiLiangCheng) add一个文件后未commit后再次add 是否要创建新的Blob？
     *
     * @param addFileName 提交的文件名
     */
    public void add(String addFileName) {
        if (addFileName == null || addFileName.isEmpty()) {
            throw new GitletException("Please enter a file name.");
        }

        File fileAdded = join(CWD, addFileName);

        if (!fileAdded.exists()) {
            throw new GitletException("File does not exist.");
        }

        if (removeStage.exist(addFileName)) {
            removeStage.remove(addFileName);
        }

        if (addStage.exist(addFileName)) {
            addStage.remove(addFileName);
        }

        String fileAddedContent = readContentsAsString(fileAdded);
        Commit headCommit = getHeadCommit();
        HashMap<String, String> headCommitBlobMap = headCommit.getBlobMap();

        if (headCommitBlobMap.containsKey(addFileName)) {
            String headCommitFileBlobId = headCommitBlobMap.get(addFileName);
            //内容是否一致？
            //直接比内容还是比hash 我感觉其实都一样
            String addContentHash = sha1(fileAddedContent);
            if (headCommitFileBlobId.equals(addContentHash)) {
                return;
            }
        }
        Blob blob = new Blob(fileAddedContent);
        blob.save();
        BlobPointer blobPointer = new BlobPointer(blob.getId());
        addStage.save(addFileName, blobPointer);

    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): commit command
     * 1. add stage, remove stage为空 -- "No changes added to the commit."
     * 2. commit message 为空 -- "Please enter a commit message."
     * 3. commit的blob map里面要存这个版本所有的文件的位置(包括之前已经commit过的文件）
     * 4. 根据add stage, remove stage更新 新的blob map
     * 5. 记得删除stage中更新过的文件的指针
     * TODO(QingZhiLiangCheng): 给commit或HEAD加入branch name属性 呃呃
     */
    public void commit(String commitMsg) {
        List<String> addStageFiles = addStage.getFiles();
        List<String> removeStageFiles = removeStage.getFiles();
        if (addStageFiles.isEmpty() && removeStageFiles.isEmpty()) {
            throw new GitletException("No changes added to the commit.");
        }
        if (commitMsg == null || commitMsg.isEmpty()) {
            throw new GitletException("Please enter a commit message.");
        }
        Commit headCommit = getHeadCommit();
        HashMap<String, String> oldHashMap = headCommit.getBlobMap();
        HashMap<String, String> newHashMap = updateHashMap(oldHashMap, addStageFiles, removeStageFiles);
        Commit newCommit = new Commit(headCommit, commitMsg, newHashMap);
        newCommit.save();
        updateHead(newCommit.getId());
        updateBranch(getHead().getBranchName(), newCommit.getId());

    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCHeng): remove files
     * 1. 文件名是空 "Please enter a file name."
     * 2. 在addStage中不存在 在commit中不存在 - "No reason to remove the file."
     * 3. 在addStage中存在 -- 删除
     * 4. 在commit中存在 -- 加入remove stage
     * 5. 如果用户还没有手动删除这个文件 从工作目录中删除文件
     */
    public void rm(String removeFileName) {
        if (removeFileName == null || removeFileName.isEmpty()) {
            throw new GitletException("Please enter a file name.");
        }
        Commit headCommit = getHeadCommit();
        HashMap<String, String> headCommitBlobMap = headCommit.getBlobMap();
        if (!addStage.exist(removeFileName) && !headCommitBlobMap.containsKey(removeFileName)) {
            throw new GitletException("No reason to remove the file.");
        }

        if (addStage.exist(removeFileName)) {
            addStage.delete(removeFileName);
        }
        if (headCommitBlobMap.containsKey(removeFileName)) {
            removeStage.save(removeFileName);
            restrictedDelete(new File(CWD, removeFileName));
        }

    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): print log
     * example format 官网上的
     * ===
     * commit a0da1ea5a15ab613bf9961fd86f010cf74c7ee48
     * Date: Thu Nov 9 20:00:05 2017 -0800
     * A commit message.
     * <p>
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): 对于合并提交（具有两个父提交的提交）
     * ===
     * commit 3e8bf1d794ca2e9ef8a4007275acf3751c7170ff
     * Merge: 4975af1 2c1ead1
     * Date: Sat Nov 11 12:30:00 2017 -0800
     * Merged development into master.
     * “Merge:” 后面的两个十六进制数字依次由第一和第二个父提交 ID 的前七位组成
     * <p>
     * 文档中说
     * "Starting at the current head commit,
     * display information about each commit backwards along the commit tree until the initial commit,
     * following the first parent commit links, ignoring any second parents found in merge commits. "
     * 所以说对于普通提交：顺着父提交打印就行
     * 对于合并提交：需要显示该提交的信息，包括两个父提交的简短哈希值，但仅沿着第一个父提交继续向上回溯历史。
     * TODO(QingZhiLiangCheng): 测试合并打印是否正确
     */
    public void printLog() {
        Commit commit = getHeadCommit();
        while (!commit.getParents().isEmpty()) {
            printCommitLog(commit);
            commit = Commit.getCommit(commit.getParents().get(0));
        }
        printCommitLog(commit);
    }

    /**
     * TODO(QingZhiLiangCheng)
     *  java gitlet.Main checkout -- [file name]
     *  java gitlet.Main checkout [commit id] -- [file name]
     *  java gitlet.Main checkout [branch name]
     * 123
     */
    public void checkout(String[] args) {
        String fileName;
        if (args.length == 4) {
            //java gitlet.Main checkout [commit id] -- [file name]
            fileName = args[2];
            String commitId = args[1];
            checkoutFileFromCommitId(commitId, fileName);
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
     * Done[Completed on 2025-05-17](QingZhiLiangCheng) 更新并存储branch pointer指向的位置
     */
    private void updateBranch(String branchName, String commitID) {
        File branchFile = join(HEADS_DIR, branchName);
        writeObject(branchFile, new Branch(branchName, commitID));
    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng) 更新并存储head pointer指向的位置
     */
    private void updateHead(String commitID) {
        Head oldHead = getHead();
        File headFile = join(HEAD_POINT, "HEAD");
        writeObject(headFile, new Head(oldHead.getBranchName(), commitID));
    }


    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): 根据add stage, remove stage 创建更新的 blob map
     */
    private HashMap<String, String> updateHashMap(HashMap<String, String> hashMap,
                                                  List<String> addStageFiles,
                                                  List<String> removeStageFiles) {
        for (String fileName : addStageFiles) {
            String id = readObject(join(ADD_STAGE_DIR, fileName), Pointer.class).next;
            hashMap.put(fileName, id);
            addStage.delete(fileName);
        }
        for (String fileName : removeStageFiles) {
            hashMap.remove(fileName);
            removeStage.delete(fileName);
        }
        return hashMap;
    }


    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): 创建目录结构<br>
     */
    public static void createInitDir() {
        GITLET_DIR.mkdirs();
        OBJECTS_DIR.mkdirs();
        COMMIT_DIR.mkdirs();
        BLOBS_DIR.mkdirs();
        REFS_DIR.mkdirs();
        HEADS_DIR.mkdirs();
        HEAD_POINT.mkdirs();
        ADD_STAGE_DIR.mkdirs();
        REMOVE_STAGE_DIR.mkdirs();
    }


    /**
     * Done[Completed on 2025-05-10](ChengShi) 创建master和HEAD<br>
     * 创建Branch master 指向init commit{@link Branch#Branch(String, String)}<br>
     * 写入.gitlet/refs/haeds/master {@link Branch#store()}<br>
     * 创建HEAD 指向init commit {@link Head#Head(String, String)} )}<br>
     * 写入.gitlet/HEAD {@link Head#score()}<br>
     *
     * @param commitId commitID
     */
    private static void initReference(String commitId) {
        try {
            Branch master = new Branch("master", commitId);
            master.store();
            Head head = new Head("master", commitId);
            head.score();
        } catch (GitletException e) {
            throw new RuntimeException("初始化Master和Head失败: " + e.getMessage(), e);
        }
    }


    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): 获取HEAD指针
     */
    public static Head getHead() {
        return readObject(join(Repository.HEAD_POINT, "HEAD"), Head.class);
    }

    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): 获取HEAD指针所指向的Commit对象
     */
    public static Commit getHeadCommit() {
        return Commit.getCommit(getHead().next);
    }


    /**
     * TODO(QingZhiLiangCheng):从指定的提交中检出（恢复）某个文件到工作目录 不修改暂存区
     * 1. 判断commit是否存在？
     * 2. 判断fileName是否存在？ - "File does not exist in that commit."
     */
    private void checkoutFileFromCommitId(String commitId, String fileName) {
        Commit headCommit = getHeadCommit();
        HashMap<String, String> headCommitBlobMap = headCommit.getBlobMap();
        if (!headCommitBlobMap.containsKey(fileName)) {
            throw new GitletException("File does not exist in that commit.");
        }
        String blobId = headCommitBlobMap.get(fileName);
        Blob blob = Blob.getBlobById(blobId);
        File targetFile = join(CWD, fileName);
        overWriteFileWithBlob(targetFile, blob.getContent());
    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): 覆盖targetFile
     */
    private void overWriteFileWithBlob(File targetFile, String content) {
        writeContents(targetFile, content);
    }
}
