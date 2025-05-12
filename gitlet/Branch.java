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
public class Branch extends Pointer {
    private final String branchName;

    /**
     * Done[Completed on 2025-05-10](QingZhiLiangCheng) Branch构造方法
     *
     * @param id         commitID
     * @param branchName branch name
     */
    public Branch(String id, String branchName) {
        super(id);
        this.branchName = branchName;
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
