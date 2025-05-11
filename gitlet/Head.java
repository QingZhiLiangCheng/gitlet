package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.writeContents;
import static gitlet.Utils.writeObject;

/**
 * TODO[Completed on 2025-05-10](QingZhiLiangCheng): add and create Head pointer.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-10
 */
public class Head extends Pointer {
    public Head(String id) {
        super(id);
    }

    /**
     * TODO[Completed on 2025-05-10](QingZhiLiangCheng)
     * FIXME(QingZhiLiangCheng) 没想好用writeObject方法还是writeContent方法
     * 写入HEAD_POINT {@link Utils#writeObject(File, Serializable)}
     */
    public void score() {
        writeObject(Repository.HEAD_POINT, this);
    }
}
