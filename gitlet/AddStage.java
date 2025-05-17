package gitlet;

import java.io.File;
import java.util.List;

import static gitlet.Utils.*;

/**
 * TODO(QingZhiLiangCheng): addStage manager.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-14
 */
public class AddStage {
    public static File path = Repository.ADD_STAGE_DIR;

    AddStage() {
    }

    /**
     * Done[Completed on 2025-05-14](QingZhiLiangCheng) add stage中是否存在file
     */
    public boolean exist(String fileName) {
        File targetFilePath = join(path, fileName);
        return targetFilePath.exists();
    }

    /**
     * Done[Completed on 2025-05-14](QingZhiLiangCheng) remove file in add stage
     */
    public void remove(String fileName) {
        join(path, fileName).delete();
    }

    /**
     * Done[Completed on 2025-05-14](QingZhiLiangCheng) add file in add stage
     */
    public void save(String addFileName, BlobPointer blobPointer) {
        File addPath = join(path, addFileName);
        writeObject(addPath, blobPointer);
    }


    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng) 获取addStage中的文件列表
     */
    public List<String> getFiles() {
        return plainFilenamesIn(path);
    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): 删除文件
     * @param fileName
     */
    public void delete(String fileName) {
        join(path, fileName).delete();
    }
}
