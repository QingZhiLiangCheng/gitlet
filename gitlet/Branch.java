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
     * @param branchName branch name
     * @param commitID   commitID
     */
    public Branch(String branchName, String commitID) {
        super(commitID);
        this.branchName = branchName;
    }

    public String getNext() {
        return super.next;
    }

    public String getBranchName() {
        return branchName;
    }


}
