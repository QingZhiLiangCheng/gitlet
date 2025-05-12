package gitlet;

import java.io.File;

import static gitlet.Utils.*;

/**
 * TODO(QingZhiLiangCheng): blob pointer
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-11
 */
public class BlobPointer extends Pointer {
    public BlobPointer(String blobPathName) {
        super(blobPathName);
    }

    /**
     * TODO(QingZhiLiangCheng): 将指针写入addStage区
     */
    public void saveInAddStage(String addFileName) {
        File path = join(Repository.ADD_STAGE_DIR,addFileName);
        writeObject(path,this);
    }
}
