package gitlet;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;


/**
 * Represents a gitlet commit object.
 *  TODO(QingZhiLiangCheng): implement Commit
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-10
 */
public class Commit implements Serializable {

    /**
     * TODO(QingZhiLiangCheng): 选择合适的数据结构和成员变量
     */
    private String id;
    private String message;
    private Timestamp timestamp;
    private List<String> parents;
    private HashMap<String, String> blobMap;


    /**
     * TODO(ChengShi) 实现初始提交 <br>
     * 初始提交要求<br>
     * message: "inital commit"<br>
     * timestamp: "(Unix)纪元" 即 00:00:00 UTC, Thursday, 1 January 1970
     * id {@link #toSha1()}
     */
    public Commit() {

    }

    /**
     * TODO(QingZhiLiangCheng) 提交构造函数
     * @param message
     * @param timestamp
     * @param directParent
     * @param blobFilename
     * @param blobId
     */
    public Commit(String message,
                  Timestamp timestamp,
                  String directParent,
                  String blobFilename,
                  String blobId) {

    }

    /**
     * TODO(ChengShi) getter setter方法<br>
     * 用注解也行 生成也行
     */

    /**
     * TODO(QingZhiLiangCheng): 保存commit
     * 将id保存至 `.gitlet/objects/commit/id`
     * {@link Utils#writeObject(File, Serializable)}
     */
    public void saveCommit() {

    }

    /**
     * TODO(ChengShi) toSha1方法 <br>
     * 将message和TimeStamp混合哈希<br>
     * sha1函数在Utils类中提供好了
     */
    public String toSha1() {
        return null;
    }


}
