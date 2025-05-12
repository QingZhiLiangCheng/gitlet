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
public class Head extends Pointer {
    public Head(String id) {
        super(id);
    }


    /**
     * Done[Completed on 2025-05-10](QingZhiLiangCheng)
     * FIXME(QingZhiLiangCheng) 没想好用writeObject方法还是writeContent方法
     * 写入HEAD_POINT {@link Utils#writeObject(File, Serializable)}
     */
    public void score() {
        writeObject(Repository.HEAD_POINT, this);
    }

    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): 获取HEAD指针
     * @return
     */
    public static Head getHead(){
        return readObject(Repository.HEAD_POINT, Head.class);
    }

    /**
     * Done[Completed on 2025-05-11](QingZhiLiangCheng): 获取HEAD指针所指向的Commit对象
     * @return
     */
    public static Commit getHeadCommit(){
        return Commit.getCommit(getHead().next);
    }
}
