package gitlet;

import java.io.File;

import static gitlet.Utils.join;
import static gitlet.Utils.writeObject;

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
}
