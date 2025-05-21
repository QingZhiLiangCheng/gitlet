package gitlet;


import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static gitlet.Utils.*;


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
     * FIXME(QingZhiLiangCheng): 对于parents成员变量的类型 有疑问 需进一步考察
     * -- GETTER --
     *  Done[Completed on 2025-05-12](ChengShi) getter setter方法<br>
     *  用注解也行 生成也行
     */

    private String id;

    private String message;

    private Timestamp timestamp;

    private List<String> parents;
    private HashMap<String, String> blobMap;

    public void setId() {
        this.id = sha1(message, timestamp);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setParents(List<String> parents) {
        this.parents = parents;
    }

    public HashMap<String, String> getBlobMap() {
        return blobMap;
    }

    public void setBlobMap(HashMap<String, String> blobMap) {
        this.blobMap = blobMap;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public List<String> getParents() {
        return parents;
    }

    /**
     * Done[Completed on 2025-05-12](ChengShi) 实现初始提交 <br>
     * 初始提交要求<br>
     * message: "initial commit"<br>
     * timestamp: "(Unix)纪元" 即 00:00:00 UTC, Thursday, 1 January 1970
     */
    public Commit() {
        message = "initial commit";
        timestamp = new Timestamp(0);
        id = sha1(message, timestamp.toString());
        blobMap = new HashMap<>();
        parents = new LinkedList<>();
    }

    /**
     * TODO(QingZhiLiangCheng) 提交构造函数
     *
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
     * Done[Completed on 2025-05-17](ChengShi) 构造newCommit
     */
    public Commit(Commit oldCommit, String message, HashMap<String, String> blobMap) {
        this.message = message;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.id = sha1(message, timestamp.toString());
        this.parents = new LinkedList<>();
        this.parents.add(oldCommit.getId());
        this.blobMap = blobMap;

    }
}
