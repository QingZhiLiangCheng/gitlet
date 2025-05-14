package gitlet;

import java.io.File;

import static gitlet.Utils.join;

/**
 * TODO(QingZhiLiangCheng): removeStage manager.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-14
 */
public class RemoveStage {
    public static File path = Repository.REMOVE_STAGE_DIR;

    RemoveStage(){}

    /**
     * Done[Completed on 2025-05-14](QingZhiLiangCheng)
     */
    public boolean exist(String fileName){
        File targetFilePath = join(path,fileName);
        return targetFilePath.exists();
    }

    /**
     * Done[Completed on 2025-05-14](QingZhiLiangCheng) add file in remove stage
     */
    public void remove(String fileName) {
        join(path,fileName).delete();
    }
}
