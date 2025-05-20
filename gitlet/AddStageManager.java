package gitlet;

import java.io.File;
import java.util.List;

import static gitlet.Repository.GITLET_DIR;
import static gitlet.Utils.*;

/**
 * TODO(QingZhiLiangCheng): addStage manager.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-14
 * <p>
 * 重构: 2025-05-20
 * 将AddStage重构为AddStageManager:专门管理add stage文件夹下的操作
 */
public class AddStageManager {
    public static File addStageDir;

    AddStageManager() {
        addStageDir = join(GITLET_DIR, "addstage");
    }

    /**
     * TODO(QingZhiLiangCheng): 初始化
     * 创建add stage目录
     */
    public void init() {
        addStageDir.mkdirs();
    }

    /**
     * Done[Completed on 2025-05-14](QingZhiLiangCheng) add stage中是否存在file
     */
    public boolean exist(String fileName) {
        File targetFilePath = join(addStageDir, fileName);
        return targetFilePath.exists();
    }

    /**
     * Done[Completed on 2025-05-14](QingZhiLiangCheng) add file in add stage
     */
    public void save(String addFileName, BlobPointer blobPointer) {
        File addPointer = join(addStageDir, addFileName);
        writeObject(addPointer, blobPointer);
    }

    /**
     * Done[Completed on 2025-05-14](QingZhiLiangCheng) remove file in add stage
     */
    public void remove(String fileName) {
        join(addStageDir, fileName).delete();
    }


    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng) 获取addStage中的文件列表
     */
    public List<String> getFiles() {
        return plainFilenamesIn(addStageDir);
    }

    /**
     * Done[Completed on 2025-05-20](QingZhiLiangCheng) 获得指向blob的指针
     */
    public Pointer getFilePointerToBlob(String fileName) {
        return readObject(join(addStageDir, fileName), Pointer.class);
    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): 删除文件
     */
    public void delete(String fileName) {
        join(addStageDir, fileName).delete();
    }
}
