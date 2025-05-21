package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Repository.REFS_DIR;
import static gitlet.Utils.join;
import static gitlet.Utils.writeObject;

/**
 * TODO(QingZhiLiangCheng):Branch Manager, 管理Branch的相关操作
 *
 * @author QingZhiLiangCheng
 */
public class BranchManager {

    private final File HEADS_DIR;

    BranchManager() {
        HEADS_DIR = join(REFS_DIR, "heads");
    }
    public File getHEADS_DIR() {
        return HEADS_DIR;
    }

    /**
     * Done[Completed on 2025-05-20](QingZhiLiangCheng)
     */
    public void init() {
        HEADS_DIR.mkdirs();
    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng) 更新并存储branch pointer指向的位置
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     */
    public void updateBranch(String branchName, String commitID) {
        File branchFile = join(HEADS_DIR, branchName);
        writeObject(branchFile, new Branch(branchName, commitID));
    }

    /**
     * Done[Completed on 2025-05-10](QingZhiLiangCheng) store branch <br>
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     * FIXME(QingZhiLiangCheng) 没想好用writeObject方法还是writeContent方法
     * 写入 `.gitlet/refs/heads/master` {@link Utils#writeObject(File, Serializable)}<br>
     * 写入 File地址为 join(HEADS_DIR,branchName)
     */
    public void saveBranch(Branch branch) {
        File branchHead = join(HEADS_DIR, branch.getBranchName());
        writeObject(branchHead, branch);
    }

    /**
     * TODO(ChengShi): 是否存在该branch
     */
    public boolean exist(String branchName) {
        return false;
    }

    /**
     * TODO(ChengShi): 根据branch name获取Branch
     */
    public Branch getBranchFromName(String branchName) {
        return null;
    }



    /**
     * Done[Completed on 2025-05-21](ChengShi):add new branch
     * 创建一个指定名称的新分支，并让它指向当前的HEAD提交
     * 这个命令不会立即切换到新创建的分支（就像真实的 Git 一样）
     * --直到java gitlet.Main checkout branchName 才会切换了分支
     * 用得到的函数应该是都写过了 如果没有的话再自己加新的
     */
    public void createBranch(String newBranchName) {
        File newBranch = join(HEADS_DIR, newBranchName);
        if (newBranch.exists()) {
            throw new GitletException(newBranchName + " already exists.");
        }
        HeadManager headManager = new HeadManager();
        String currentCommitId = headManager.getHead().next;
        writeContents(newBranch, currentCommitId);
    }

    /**
     * Done[Completed on 2025-05-21](ChengShi): remove branch
     * 删除指定名称的分支。
     * 这仅仅意味着删除与该分支相关联的指针；并不会删除在该分支下创建的所有提交等内容。
     */
    public void removeBranch(String branchName) {
        File branchFile = join(HEADS_DIR, branchName);
        if (!branchFile.exists()) {
            throw new GitletException(branchName + " does not exist.");
        }
        HeadManager headManager = new HeadManager();
        String currentBranch = headManager.getHead().getBranchName();
        if (branchName.equals(currentBranch)) {
            throw new GitletException("Cannot remove the current branch.");
        }

        branchFile.delete();
    }
}

