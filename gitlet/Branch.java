package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

/**
 * TODO(QingZhiLiangCheng): add and create Branch pointer.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-10
 */
public class Branch extends Pointer {
    private final String branchName;

    /**
     * TODO[Completed on 2025-05-10](QingZhiLiangCheng) Branch构造方法
     *
     * @param id         commitID
     * @param branchName branch name
     */
    public Branch(String id, String branchName) {
        super(id);
        this.branchName = branchName;
    }

    /**
     * TODO[Completed on 2025-05-10](QingZhiLiangCheng) store branch <br>
     * FIXME(QingZhiLiangCheng) 没想好用writeObject方法还是writeContent方法
     * 写入 `.gitlet/refs/heads/master` {@link Utils#writeObject(File, Serializable)}<br>
     * 写入 File地址为 join(HEADS_DIR,branchName)
     */
    public void store() {
        File branchHead = join(Repository.HEADS_DIR, branchName);
        writeObject(branchHead, this);
    }
}
