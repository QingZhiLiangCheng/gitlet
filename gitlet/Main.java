package gitlet;

import static gitlet.Repository.init;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author QingZhiLiangCheng
 * @since 2025-05-09
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        // TODO(QingZhiLiangCheng): what if args is empty?
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                /*
                 * TODO(QingZhiLiangCheng: init): 处理 `init` 命令
                 * 架构图在`/note/.md`中都画好了
                 * 如果存在.gitlet 视为错误 退出程序 打印错误信息
                 * "A Gitlet version-control system already exists in the current directory."
                 * 创建各个文件夹
                 * 创建 Commit 0 提交信息为"init commit" timestamp为"(Unix)纪元" 即 00:00:00 UTC, Thursday, 1 January 1970
                 * 创建初始 Branch -- master
                 * 创建头 HEAD
                 */
                init();
                break;
            case "add":
                /*
                 * TODO(QingZhiLiangCheng: add): 处理 `add [filename]` 命令
                 */
                break;
            case "commit":
                // TODO(QingZhiLiangCheng): 处理 `commit [message]` 命令
                break;
            case "rm":
                // TODO(QingZhiLiangCheng): 处理 `rm [filename]` 命令
                break;
            case "log":
                // TODO(QingZhiLiangCheng): 处理 `log` 命令
                break;
            case "find":
                // TODO(QingZhiLiangCheng): 处理 `find [commit message]` 命令
                break;
            case "status":
                // TODO(ChengShi: status): 处理 `status` 命令
                break;
            case "checkout":
                // TODO(QingZhiLiangCheng): 处理 `checkout` 命令
                break;
            case "branch":
                // TODO(QingZhiLiangCheng): 处理 `branch [branch name]` 命令
                break;
            case "rm-branch":
                // TODO(QingZhiLiangCheng): 处理 `rm-branch [branch name]` 命令
                break;
            // TODO(QingZhiLiangCheng): FILL THE REST IN
        }
    }
}
