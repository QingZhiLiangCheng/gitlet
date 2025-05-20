package gitlet;

import java.io.File;

import static gitlet.Repository.OBJECTS_DIR;
import static gitlet.Utils.join;

/**
 * TODO(QingZhiLiangCheng):Blob Manager, 管理Blob的相关操作
 *
 * @author QingZhiLiangCheng
 */
public class BlobManager {
    public static final File BLOBS_DIR = join(OBJECTS_DIR, "blobs");
    ;

    BlobManager() {

    }

    public void init() {
        BLOBS_DIR.mkdirs();
    }

    /**
     * Done[Completed on 2025-05-15](ChengShi): 在filePath写入content
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     * {@link Utils#writeContents(File, Object...)}
     */
    public void saveBlob(Blob blob) {
        File parentDir = blob.getFilePath().getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();  // 创建必要的父目录
        }
        Utils.writeContents(blob.getFilePath(), blob.getContent());
    }

}
