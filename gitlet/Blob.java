package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

/**
 * TODO(QingZhiLiangCheng): 搭建Branch pointer骨架 .
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-10
 */
public class Blob implements Serializable {

    /**
     * TODO(QingZhiLiangCheng): 选择合适的成员变量和数据结构<br>
     * content: 内容<br>
     * Utiles 提供了两个方法 {@link Utils#readContents} {@link Utils#readContentsAsString(File)}<br>
     * 所以我觉得content用String和byte[]两个类型其实都行<br>
     * readContentsAsString其实就是调用的readContents 所以我选了String类型<br>
     * <br>
     * id: hash值<br>
     * filePath
     */
    private String content;

    private String id;
    private File filePath;

    /**
     * Done[Completed on 2025-05-15](ChengShi): Blob构造函数
     * hash content
     * Blob文件路径: 文件位置？ 文件名:id
     * {@link Utils#join(File, String...)}
     * hash
     *
     * @param content content
     */
    public Blob(String content) {
        this.content=content;
        this.id=Utils.sha1(content);
        this.filePath=Utils.join(BlobManager.BLOBS_DIR, this.id);
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }


    public File getFilePath() {
        return filePath;
    }

    public void setFilePath(File filePath) {
        this.filePath = filePath;
    }



    /**
     * Done(ChengShi)
     */
    public static String getContentFromId(String blobId) {
        return readContentsAsString(join(BlobManager.BLOBS_DIR, blobId));
    }
}
