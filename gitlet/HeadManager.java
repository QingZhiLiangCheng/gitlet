package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Repository.REFS_DIR;
import static gitlet.Utils.*;

/**
 * TODO(QingZhiLiangCheng):Head Manager, 管理HEAD的相关操作
 *
 * @author QingZhiLiangCheng
 */
public class HeadManager {

    private final File HEAD_POINT;

    HeadManager() {
        HEAD_POINT = join(REFS_DIR, "HEAD");
    }

    /**
     * Done[Completed on 2025-05-20](QingZhiLiangCheng)
     */
    public void init() {
        HEAD_POINT.mkdirs();
    }

    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): 获取HEAD指针
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     */
    public Head getHead() {
        return readObject(join(HEAD_POINT, "HEAD"), Head.class);
    }

    /**
     * Done[Completed on 2025-05-10](QingZhiLiangCheng)
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     * FIXME(QingZhiLiangCheng) 没想好用writeObject方法还是writeContent方法
     * 写入HEAD_POINT {@link Utils#writeObject(File, Serializable)}
     */
    public void saveHead(Head head) {
        File path = join(HEAD_POINT, "HEAD");
        writeObject(path, head);
    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng) 更新并存储head pointer指向的位置
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     */
    public void updateHead(String commitID) {
        Head oldHead = getHead();
        File headFile = join(HEAD_POINT, "HEAD");
        writeObject(headFile, new Head(oldHead.getBranchName(), commitID));
    }
}
