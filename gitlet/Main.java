package gitlet;


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
        // Done[Completed on 2025-05-11](QingZhiLiangCheng): what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        Repository repository= new Repository();
        switch (firstArg) {
            case "init":
                /*
                 * Done[Completed on 2025-05-11](QingZhiLiangCheng: init): 处理 `init` 命令
                 * 架构图在`notes/gitlet.md`中都画好了
                 * 如果存在.gitlet 视为错误 退出程序 打印错误信息
                 * "A Gitlet version-control system already exists in the current directory."
                 * 创建各个文件夹
                 * 创建 Commit 0 提交信息为"init commit" timestamp为"(Unix)纪元" 即 00:00:00 UTC, Thursday, 1 January 1970
                 * 创建初始 Branch -- master
                 * 创建头 HEAD
                 */
                repository.init();
                break;
            case "add":
                /*
                 * Done[Completed on 2025-05-14](QingZhiLiangCheng: add): 处理 `add [filename]` 命令
                 */
                repository.add(args[1]);
                break;
            case "commit":
                // Done[Completed on 2025-05-17](QingZhiLiangCheng): 处理 `commit [message]` 命令
                repository.commit(args[1]);
                break;
            case "rm":
                // Done[Completed on 2025-05-17](QingZhiLiangCheng): 处理 `rm [filename]` 命令
                repository.rm(args[1]);
                break;
            case "log":
                // Done[Completed on 2025-05-17](QingZhiLiangCheng): 处理 `log` 命令
                repository.printLog();
                break;
            case "find":
                // Done(ChengShi): 处理 `find [commit message]` 命令
                repository.findCommitsByMessage(args[1]);
                break;
            case "status":
                // Done(ChengShi: status): 处理 `status` 命令
                repository.showStatus();
                break;
            case "checkout":
                // Done(QingZhiLiangCheng): 处理 `checkout` 命令
                repository.checkout(args);
                break;
            case "branch":
                // Done(ChengShi): 处理 `branch [branch name]` 命令
                repository.createBranch(args[1]);
                break;
            case "rm-branch":
                // Done(ChengShi): 处理 `rm-branch [branch name]` 命令
                repository.removeBranch(args[1]);
                break;
            case "merge":
                // TODO(QingZhiLiangCheng): 处理 `merge [branch name]` 命令
                repository.merge(args[1]);
                break;
            // TODO(QingZhiLiangCheng)[Completed on 2025-05-11]: NO command
            default:
                System.out.println("No command with that name exists.");
                break;
        }
    }
}
