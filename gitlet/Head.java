package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

/**
 * TODO(QingZhiLiangCheng): 搭建Head pointer框架.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-10
 */
public class Head extends Pointer{
    private final String branchName;

    public Head(String branchName,String commitID) {
        super(commitID);
        this.branchName = branchName;
    }


    /**
     * Done[Completed on 2025-05-10](QingZhiLiangCheng)
     * FIXME(QingZhiLiangCheng) 没想好用writeObject方法还是writeContent方法
     * 写入HEAD_POINT {@link Utils#writeObject(File, Serializable)}
     */
    public void save() {
        File path = join(Repository.HEAD_POINT,"HEAD");
        writeObject(path, this);
    }


    public String getBranchName() {
        return branchName;
    }
}
