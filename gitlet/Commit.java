package gitlet;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
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
     * TODO[Completed on 2025-05-11](QingZhiLiangCheng): 保存commit
     * FIXME(QingZhiLiangCheng): 要不要构建动态哈希表 更快的找CommitID？
     * 将id保存至 `.gitlet/objects/commit/id`
     * {@link Utils#writeObject(File, Serializable)}
     */
    public void saveCommit() {
        File commitFile = new File(Repository.COMMIT_DIR,id);
        writeObject(commitFile,this);
    }

    /**
     * TODO(ChengShi) toSha1方法 <br>
     * 将message和TimeStamp混合哈希<br>
     * sha1函数在Utils类中提供好了
     */
    public String toSha1() {
        return null;
    }

    /**
     * TODO(QingZhiLiangCheng) 获取commit文件
     * @param id commit id
     * @return commit文件
     */
    public static Commit getCommit(String id){
        File commitFile = join(Repository.COMMIT_DIR,id);
        return readObject(commitFile, Commit.class);
    }


}
