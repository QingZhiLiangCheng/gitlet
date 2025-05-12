package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

/**
 * Done[Completed on 2025-05-10](QingZhiLiangCheng): 搭建Branch pointer骨架.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-10
 */
public class Branch implements Serializable {
    private final String branchName;

    private final Pointer next;

    /**
     * Done[Completed on 2025-05-10](QingZhiLiangCheng) Branch构造方法
     *
     * @param branchName branch name
     * @param commitID   commitID
     */
    public Branch(String branchName, String commitID) {
        this.next = new Pointer(commitID);
        this.branchName = branchName;
    }

    public Pointer getNext() {
        return next;
    }

    public String getBranchName() {
        return branchName;
    }

    /**
     * Done[Completed on 2025-05-10](QingZhiLiangCheng) store branch <br>
     * FIXME(QingZhiLiangCheng) 没想好用writeObject方法还是writeContent方法
     * 写入 `.gitlet/refs/heads/master` {@link Utils#writeObject(File, Serializable)}<br>
     * 写入 File地址为 join(HEADS_DIR,branchName)
     */
    public void store() {
        File branchHead = join(Repository.HEADS_DIR, branchName);
        writeObject(branchHead, this);
    }
}
