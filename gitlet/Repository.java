package gitlet;


import java.io.File;
import java.util.*;

import static gitlet.Utils.*;
import static java.lang.System.exit;

import java.util.List;


/**
 * Represents a gitlet repository.
 * TODO(QingZhiLiangCheng): 搭建Repository class框架.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-10
 * 重构: 2025-05-20
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
    private static File CWD = new File(System.getProperty("user.dir"));
    ;
    /**
     * the .gitlet directory.
     */
    public static File GITLET_DIR = join(CWD, ".gitlet");
    ;

    /**
     * the objects directory<br>
     * 包含commits 和 blogs
     */
    public static File OBJECTS_DIR = join(GITLET_DIR, "objects");


    /**
     * the refs directory.<br>
     * 包含heads 和 HEAD<br>
     * - heads 存分支
     */
    public static File REFS_DIR = join(GITLET_DIR, "refs");


    /**
     * TODO(QingZhiLiangCheng): 重构
     * 增加add manager, remove manager, blob manager, commit manager, head manager,branch manager来管理对于仓库的操作
     * commit, head, branch, blob 等类仅仅作为实体
     */
    private final CommitManager commitManager;

    private final BlobManager blobManager;

    private final AddStageManager addStageManager;

    private final HeadManager headManager;

    private final BranchManager branchManager;
    public static RemoveStageManager removeStageManager;

    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): 构造函数<br>
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     */
    public Repository() {
        CWD = new File(System.getProperty("user.dir"));
        commitManager = new CommitManager();
        blobManager = new BlobManager();
        headManager = new HeadManager();
        branchManager = new BranchManager();
        addStageManager = new AddStageManager();
        removeStageManager = new RemoveStageManager();
    }

    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): config directory
     * 配置好每个文件夹的位置
     */

    /*
    private void configDIRS() {
        GITLET_DIR = join(CWD, ".gitlet");
        OBJECTS_DIR = join(GITLET_DIR, "objects");


        REFS_DIR = join(GITLET_DIR, "refs");


        ADD_STAGE_DIR = join(GITLET_DIR, "addstage");
        REMOVE_STAGE_DIR = join(GITLET_DIR, "removestage");
    }*/

    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): init<br>
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     * 架构图在`READEME.md`中都画好了<br>
     * 如果存在.gitlet 视为错误 退出程序 打印错误信息<br>
     * "A Gitlet version-control system already exists in the current directory."<br>
     * 创建各个文件夹
     * 创建 Commit 0<br>
     * 存储commit
     * 创建master和HEAD {@link #initReference(String)} ()}<br>
     */
    public void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            exit(0);
        }
        createInitDir();
        Commit initCommit = new Commit();
        commitManager.saveCommit(initCommit);
        initReference(initCommit.getId());
    }

    /**
     * Done[Completed on 2025-05-14](QingZhiLiangCheng) add command<br>
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
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

        if (removeStageManager.exist(addFileName)) {
            removeStageManager.remove(addFileName);
        }

        if (addStageManager.exist(addFileName)) {
            addStageManager.remove(addFileName);
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
        blobManager.saveBlob(blob);
        BlobPointer blobPointer = new BlobPointer(blob.getId());
        addStageManager.save(addFileName, blobPointer);

    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): commit command
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     * 1. add stage, remove stage为空 -- "No changes added to the commit."
     * 2. commit message 为空 -- "Please enter a commit message."
     * 3. commit的blob map里面要存这个版本所有的文件的位置(包括之前已经commit过的文件）
     * 4. 根据add stage, remove stage更新 新的blob map
     * 5. 记得删除stage中更新过的文件的指针
     * TODO(QingZhiLiangCheng): 给commit或HEAD加入branch name属性 呃呃
     */
    public void commit(String commitMsg) {
        List<String> addStageFiles = addStageManager.getFiles();
        List<String> removeStageFiles = removeStageManager.getFiles();
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
        commitManager.saveCommit(newCommit);
        headManager.updateHead(newCommit.getId());
        branchManager.updateBranch(headManager.getHead().getBranchName(), newCommit.getId());

    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCHeng): remove files
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
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
        if (!addStageManager.exist(removeFileName) && !headCommitBlobMap.containsKey(removeFileName)) {
            throw new GitletException("No reason to remove the file.");
        }

        if (addStageManager.exist(removeFileName)) {
            addStageManager.delete(removeFileName);
        }
        if (headCommitBlobMap.containsKey(removeFileName)) {
            removeStageManager.save(removeFileName);
            restrictedDelete(new File(CWD, removeFileName));
        }

    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): print log
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
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
            commit = commitManager.getCommit(commit.getParents().get(0));
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
            fileName = args[3];
            String commitId = args[1];
            checkoutFileFromCommitId(commitId, fileName);
        } else if (args.length == 2) {
            //java gitlet.Main checkout [branch name】
            String branchName = args[1];
            checkoutBranch(branchName);
        }
    }

    /**
     * TODO(ChengShi): status command
     * TODO(ChengShi): 先实现前三部分(Branches, Staged Files, Removed Files)
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     * Branches: 显示当前存在的分支 并用*标记当前分支
     * Staged Files: 显示已暂存待添加的文件
     * Remove Files: 显示已暂存待删除的文件
     * 各部分之间有一个空行，整个状态也以空行结尾。
     * 条目应按字典顺序列出，使用 Java 字符串比较顺序（星号不计算）
     * 有需要的方法如果没写过的话等自己加在合适的位置就行
     */
    /*
     * 官方example
     * === Branches ===
     * *master
     * other-branch
     *
     * === Staged Files ===
     * wug.txt
     * wug2.txt
     *
     * === Removed Files ===
     * goodbye.txt
     *
     * === Modifications Not Staged For Commit ===
     * junk.txt (deleted)
     * wug3.txt (modified)
     *
     * === Untracked Files ===
     * random.stuff
     *
     */
    public void showStatus() {
       /* // === Branches ===
        System.out.println("=== Branches ===");
        List<String> branches = plainFilenamesIn(HEADS_DIR);
        String currentBranch = getHead().getBranchName();
        Collections.sort(branches);

        for (String branch : branches) {
            if (branch.equals(currentBranch)) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
        System.out.println();

        // === Staged Files ===
        System.out.println("=== Staged Files ===");
        List<String> stagedFiles = new ArrayList<>(addStageManager.getFiles());
        stagedFiles.sort(String.CASE_INSENSITIVE_ORDER);//忽略大小写
        for (String file : stagedFiles) {
            System.out.println(file);
        }
        System.out.println();

        // === Removed Files ===
        System.out.println("=== Removed Files ===");
        List<String> removedFiles = new ArrayList<>(removeStage.getFiles());
        removedFiles.sort(String.CASE_INSENSITIVE_ORDER);
        for (String file : removedFiles) {
            System.out.println(file);
        }
        System.out.println();*/
    }


    /**
     * TODO(ChengShi):add new branch
     * 创建一个指定名称的新分支，并让它指向当前的HEAD提交
     * 这个命令不会立即切换到新创建的分支（就像真实的 Git 一样）
     * --直到java gitlet.Main checkout branchName 才会切换了分支
     * 用得到的函数应该是都写过了 如果没有的话再自己加新的
     */
    public void createBranch(String newBranchName) {
       /* File newBranch = join(HEADS_DIR, newBranchName);
        if (newBranch.exists()) {
            throw new GitletException(newBranchName + " already exists.");
        }
        String currentCommitId = getHead().getBranchName();
        writeContents(newBranch, currentCommitId);*/
    }

    /**
     * TODO(ChengShi): remove branch
     * 删除指定名称的分支。
     * 这仅仅意味着删除与该分支相关联的指针；并不会删除在该分支下创建的所有提交等内容。
     */
    public void removeBranch(String branchName) {
/*        File branchFile = join(HEADS_DIR, branchName);
        if (!branchFile.exists()) {
            throw new GitletException(branchName + " does not exist.");
        }

        String currentBranch = getHead().getBranchName();
        if (branchName.equals(currentBranch)) {
            throw new GitletException("Cannot remove the current branch.");
        }

        branchFile.delete();*/
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
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): 根据add stage, remove stage 创建更新的 blob map
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     */
    private HashMap<String, String> updateHashMap(HashMap<String, String> hashMap,
                                                  List<String> addStageFiles,
                                                  List<String> removeStageFiles) {
        for (String fileName : addStageFiles) {
            String id = addStageManager.getFilePointerToBlob(fileName).next;
            hashMap.put(fileName, id);
            addStageManager.delete(fileName);
        }
        for (String fileName : removeStageFiles) {
            hashMap.remove(fileName);
            removeStageManager.delete(fileName);
        }
        return hashMap;
    }


    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): 创建目录结构<br>
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     */
    private void createInitDir() {
        GITLET_DIR.mkdirs();
        OBJECTS_DIR.mkdirs();
        REFS_DIR.mkdirs();

        commitManager.init();
        headManager.init();
        branchManager.init();
        addStageManager.init();
        removeStageManager.init();
        blobManager.init();
    }


    /**
     * Done[Completed on 2025-05-10](ChengShi) 创建master和HEAD<br>
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     * 创建Branch master 指向init commit{@link Branch#Branch(String, String)}<br>
     * 写入.gitlet/refs/haeds/master
     * 创建HEAD 指向init commit {@link Head#Head(String, String)} )}<br>
     * 写入.gitlet/HEAD
     *
     * @param commitId commitID
     */
    private void initReference(String commitId) {
        try {
            Branch master = new Branch("master", commitId);
            branchManager.saveBranch(master);
            Head head = new Head("master", commitId);
            headManager.saveHead(head);
        } catch (GitletException e) {
            throw new RuntimeException("初始化Master和Head失败: " + e.getMessage(), e);
        }
    }


    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): 获取HEAD指针所指向的Commit对象
     */
    public Commit getHeadCommit() {
        return commitManager.getCommit(headManager.getHead().next);
    }


    /**
     * TODO(QingZhiLiangCheng):从指定的提交中检出（恢复）某个文件到工作目录 不修改暂存区
     * 1. 判断commit是否存在？
     * 2. 判断fileName是否存在？ - "File does not exist in that commit."
     * 3. 获取old commit中该文件的blob中的内容
     * 4. 修改文件
     */
    private void checkoutFileFromCommitId(String commitId, String fileName) {
        Commit commit = commitManager.getCommit(commitId);
        HashMap<String, String> commitBlobMap = commit.getBlobMap();
        if (!commitBlobMap.containsKey(fileName)) {
            throw new GitletException("File does not exist in that commit.");
        }
        String blobId = commitBlobMap.get(fileName);
        String blobContent = Blob.getContentFromId(blobId);
        File targetFile = join(CWD, fileName);
        overWriteFileWithBlob(targetFile, blobContent);
    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): 覆盖targetFile
     */
    private void overWriteFileWithBlob(File targetFile, String content) {
        writeContents(targetFile, content);
    }

    /**
     * TODO(QingZhiLiangCheng) java gitlet.Main checkout [branch name]
     * 判断指定分支是否存在 - "No such branch exists."
     * 指定分支branchName与当前Head的branchName相同 - "No need to checkout the current branch."
     * 将指定分支头部提交的所有文件放入工作目录，并覆盖已存在的文件版本（如果存在）
     * 所有在当前分支中跟踪但在签出分支中不存在的文件都将被删除
     * 如果签出分支中的文件与当前工作目录中未被追踪（untracked）的文件发生冲突 - “There is an untracked file in the way; delete it, or add and commit it first.”
     * 指定分支视为当前分支 (HEAD)
     */
    private void checkoutBranch(String branchName) {
        if (!branchManager.contains(branchName)) {
            throw new GitletException("No such branch exists.");
        }
        String headBranchName = headManager.getHead().getBranchName();
        if (branchName.equals(headBranchName)) {
            throw new GitletException("No need to checkout the current branch.");
        }
        Branch givenBranch = branchManager.getBranchFromName(branchName);
        Commit headOfGivenBranchCommit = commitManager.getCommit(givenBranch.getNext());
        if (unTrackFileExists(headOfGivenBranchCommit)) {
            throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
        }
        updateCWD(headOfGivenBranchCommit);
        Head newHeadPointer = new Head(branchName, headOfGivenBranchCommit.getId());
        headManager.saveHead(newHeadPointer);

    }

    /**
     * TODO(QingZhiLiangCheng): 更新工作区中文件
     * 这里选择了先清空CWD中的文件
     * 然后再根据迁出分支的commit添加文件
     */
    private void updateCWD(Commit headOfGivenBranchCommit) {
        List<String> workFileNames = plainFilenamesIn(CWD);
        for (String workFile : workFileNames) {
            restrictedDelete(join(CWD, workFile));
        }
        HashMap<String, String> headOfGivenBranchCommitHashMap = headOfGivenBranchCommit.getBlobMap();
        Set<String> trackedFiles = headOfGivenBranchCommitHashMap.keySet();
        for (String trackedFile : trackedFiles) {
            File workFile = join(CWD, trackedFile);
            String boshId = headOfGivenBranchCommitHashMap.get(trackedFile);
            String content = Blob.getContentFromId(boshId);
            writeObject(workFile, content);
        }
    }


    /**
     * TODO(ChengShi): 检查当前工作目录中是否有未被track的文件
     * 未被track是指该文件没有被提交到版本中
     */
    private boolean unTrackFileExists(Commit commit) {
        return false;
    }


}
