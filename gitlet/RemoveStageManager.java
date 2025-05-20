package gitlet;

import java.io.File;
import java.util.List;

import static gitlet.Repository.GITLET_DIR;
import static gitlet.Utils.*;

/**
 * TODO(QingZhiLiangCheng): removeStage manager.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-14
 */
public class RemoveStageManager {
    public static final File REMOVE_STAGE_DIR = join(GITLET_DIR, "removestage");

    RemoveStageManager() {
    }
    public void init() {
        REMOVE_STAGE_DIR.mkdirs();
    }

    /**
     * Done[Completed on 2025-05-14](QingZhiLiangCheng)
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     */
    public boolean exist(String fileName) {
        File targetFilePath = join(REMOVE_STAGE_DIR, fileName);
        return targetFilePath.exists();
    }

    /**
     * Done[Completed on 2025-05-14](QingZhiLiangCheng) add file in remove stage
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     */
    public void remove(String fileName) {
        join(REMOVE_STAGE_DIR, fileName).delete();
    }


    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng) 获取removeStage中的文件列表
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     */
    public List<String> getFiles() {
        return plainFilenamesIn(REMOVE_STAGE_DIR);
    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): 删除文件
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     */
    public void delete(String fileName) {
        join(REMOVE_STAGE_DIR, fileName).delete();
    }

    /**
     * Done[Completed on 2025-05-17](QingZhiLiangCheng): 删除文件暂存
     * Refactor[Completed on 2025-05-20](QingZhiLiangCheng)
     */
    public void save(String removeFileName) {
        File removePointer = join(REMOVE_STAGE_DIR, removeFileName);
        writeContents(removePointer, "will delete");
    }


}
