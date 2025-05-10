package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.writeContents;
import static gitlet.Utils.writeObject;

/**
 * TODO(QingZhiLiangCheng): add and create Head pointer.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-10
 */
public class Head extends Pointer {
    public Head(String id) {
        super(id);
    }

    /**
     * TODO(QingZhiLiangCheng)
     * 写入HEAD_POINT {@link Utils#writeObject(File, Serializable)}
     */
    public void score() {
        writeObject(Repository.HEAD_POINT, this);
    }
}
