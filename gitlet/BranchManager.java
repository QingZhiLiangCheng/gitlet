package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import static gitlet.Repository.REFS_DIR;
import static gitlet.Utils.*;

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
     * Done[Completed on 2025-5-21](ChengShi): 是否存在该branch
     */
    public boolean exist(String branchName) {
        File targetFilePath = join(HEADS_DIR, branchName);
        return targetFilePath.exists();
    }

    /**
     * Done[Completed on 2025-5-21](ChengShi): 根据branch name获取Branch
     */
    public Branch getBranchFromName(String branchName) {
        File branch = join(HEADS_DIR, branchName);
        return readObject(branch, Branch.class);
    }


    /**
     * Done[Completed on 2025-5-21](QingZhiLiangCheng)
     */
    public void deleteBranch(Branch branch) {
        File file = join(HEADS_DIR, branch.getBranchName());
        file.delete();
    }

    /**
     * Done[Completed on 2025-5-21](QingZhiLiangCheng)
     */
    public List<String> getBranchList() {
        return plainFilenamesIn(HEADS_DIR);
    }
}

