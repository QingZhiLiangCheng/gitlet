package gitlet;

import java.io.File;

/**
 * TODO(QingZhiLiangCheng): add and create Branch pointer.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-10
 */
public class Blob {

    /**
     * TODO(QingZhiLiangCheng): 选择合适的成员变量和数据结构<br>
     * content: 内容<br>
     * Utiles 提供了两个方法 {@link Utils#readContents} {@link Utils#readContentsAsString(File)}<br>
     * 所以我觉得content用String和byte[]两个类型其实都行<br>
     * readContentsAsString其实就是调用的readContents 所以我选了String类型<br>
     * <br>
     * id: hash值<br>
     * filePath: 拼接{@link Repository#COMMIT_DIR}和id 其实我觉得这个属性可有可无<br>
     */
    private String content;
    private String id;
    private File filePath;

}
