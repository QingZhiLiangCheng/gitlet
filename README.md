
# Gitlet
**author**: QingZhiLiangCheng,  ChengShi

**since**: 2025-05-10

---
## Overview
这是针对伯克利大学CS61B, spring2021 Project2构建的Gitlet.

Gitlet是一个版本管理系统，仿照主流系统Git的功能并实现了其部分基本命令，包括`init`,  `add`,  `commit`,  `rm`,  `checkout`,  `branch`,  `reset`,  `rm-branch`,  `merge`等. 官方并没有提供实质性的框架， 而是要自己设计具体使用哪些类，使用哪些数据结构.

### Features
- **提交机制**：保存文件目录的快照，以后可以恢复。
- **分支管理**：在单独的分支中维护提交序列。
- **签出系统**：将单个文件或整个分支恢复到特定提交时的状态。
- **合并功能**：合并来自不同分支的更改。
- **日志历史**：查看提交历史。
- **数据持久性**：利用 Java 序列化来持久保存数据，模拟存储库的平面目录结构。
- **错误处理**：使用 Java 的异常机制实现健壮的系统，以确保稳定性和可靠性。

### GetStart
在运行Gitlet之前, 确保在根目录下运行下面的shell命令. 将会在根目录下建立一个classes文件夹 然后将所有`.java`文件通过javac编译成`.class`文件并存入classes文件夹

我是在Ubantu Linux的运行环境下执行的命令
```bash
#cd 根目录 gitlet
mkdir classes
javac -d classes gitlet/*.java
```

```bash
cd classes
ls
```

![gitlet_ls.png](note%2Fattachment%2Fgitlet_ls.png)

为了开始使用Gitlet，需要先通过命令初始化一个新的Gitlet仓库.
```bash
java gitlet.Main init # 初始化gitlet仓库
```

## Gitlet Design Document

![gitlet add1.png](note%2Fattachment%2Fgitlet%20add1.png)

![gitlet commit1.png](note%2Fattachment%2Fgitlet%20commit1.png)

![gitlet init1.png](note%2Fattachment%2Fgitlet%20init1.png)
