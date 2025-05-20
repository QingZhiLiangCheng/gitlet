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


    public String getBranchName() {
        return branchName;
    }
}
