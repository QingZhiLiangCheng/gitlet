package gitlet;

import java.io.File;

import static gitlet.Utils.*;


/**
 * Represents a gitlet repository.
 *  TODO(QingZhiLiangCheng): Implement Repository class.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-10
 */
public class Repository {

    /**
     * TODO(QingZhiLiangCheng): add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     * <br>
     * 我的目录结构
     * .gitlet (folder)
     *      |── objects (folder) // 存储commit对象文件
     *          |-- commits
     *          |-- blobs
     *      |── refs (folder)
     *          |── heads (folder) //指向目前的branch
     *              |-- master (file)
     *              |-- other file      //表示其他分支的路径
     *          |-- HEAD (file)     // 保存HEAD指针的对应hashname
     *      |-- addstage (folder)       // 暂存区文件夹
     *      |-- removestage (folder)
     */

    /**
     * current working directory.
     */
    private static File CWD;
    /**
     * the .gitlet directory.
     */
    private static File GITLET_DIR;

    /**
     * the objects directory<br>
     * 包含commits 和 blogs
     */
    public static File OBJECTS_DIR;
    public static File COMMIT_FOLDER;
    public static File BLOGS_FOLDER;

    /**
     * the refs directory.<br>
     * 包含heads 和 HEAD<br>
     * - heads 存分支
     */
    public static File REFS_DIR;
    public static File HEADS_DIR;
    public static File HEAD_POINT;

    /**
     * the stage directory.
     */
    public static File ADD_STAGE_DIR;
    public static File REMOVE_STAGE_DIR;

    /**
     * TODO(QingZhiLiangCheng): 构造函数<br>
     * 创建CWD<br>
     * 然后调用 {@link #configDIRS()}
     */
    public Repository() {

    }

    /**
     * TODO(QingZhiLiangCheng): config directory
     * 配置好每个文件夹的位置
     */
    private void configDIRS() {

    }

    /**
     * TODO(QingZhiLiangCheng): init<br>
     * 架构图在`/note/.md`中都画好了<br>
     * 如果存在.gitlet 视为错误 退出程序 打印错误信息<br>
     * "A Gitlet version-control system already exists in the current directory."<br>
     * 创建各个文件夹{@link #createInitDir()}<br>
     * 创建 Commit 0 {@link Commit#Commit()}<br>
     * 存储commit {@link Commit#saveCommit()}<br>
     * 创建master和HEAD {@link #initReference(String)} ()}<br>
     */
    public static void init() {

    }

    /**
     * TODO(QingZhiLiangCheng): 创建目录结构<br>
     */
    public void createInitDir() {

    }


    /**
     * TODO(ChengShi) 创建master和HEAD<br>
     * 创建Branch master {@link Branch#Branch(String, String)}<br>
     * 写入.gitlet/refs/haeds/master {@link Branch#store()}<br>
     * 创建HEAD {@link Head#Head(String)}<br>
     * 写入.gitlet/HEAD {@link Head#score()}<br>
     *
     * @param commitId commitID
     */
    private void initReference(String commitId) {

    }


}
