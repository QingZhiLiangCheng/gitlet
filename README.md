
# Gitlet
**Author**: QingZhiLiangCheng

**Contributors**: QingZhiLiangCheng, ChengShi

**Since**: 2025-05-10

---
## Overview
这是针对伯克利大学CS61B, spring2021 Project2构建的Gitlet.

Gitlet是一个版本管理系统，仿照主流系统Git的功能并实现了其部分基本命令，包括`init`,  `add`,  `commit`,  `rm`,  `checkout`,  `branch`,  `reset`,  `rm-branch`,  `merge`等. 官方并没有提供实质性的框架， 而是要自己设计具体使用哪些类，使用哪些数据结构.

### Features
- **提交机制**：保存文件目录的快照，以后可以恢复。
- **分支管理**：在单独的分支中维护提交序列。
- **版本切换**：将单个文件或整个分支恢复到特定提交时的状态。
- **合并功能**：合并来自不同分支的更改。
- **日志历史**：查看提交历史。
- **数据持久性**：利用 Java 序列化来持久保存数据，模拟存储库的平面目录结构。
- **错误处理**：使用 Java 的异常机制实现健壮的系统，以确保稳定性和可靠性。

### GetStart
在运行Gitlet之前, 确保在根目录下运行下面的shell命令. 将会在根目录下建立一个classes文件夹 然后将所有`.java`文件通过javac编译成`.class`文件并存入classes文件夹

我是在Ubantu Linux的运行环境下执行的命令
```bash
#cd 根目录 gitlet
mkdir gitlet-test
javac -d gitlet-test gitlet/*.java
```

```bash
cd gitlet-test
ls
```

![gitlet_ls.png](note%2Fattachment%2Fgitlet_ls.png)

为了开始使用Gitlet，需要先通过命令初始化一个新的Gitlet仓库.
```bash
java gitlet.Main init # 初始化gitlet仓库
```

## Gitlet Design Document



### structure

在开始之前，首要要弄明白git中的一些重要的对象

**sha1**

Git和Gitlet都以SHA-1的加密哈希函数，该函数可从任意字节序列生成160位整数哈希值

加密哈希函数具有这样的特性：很难找到两个具有相同哈希值的不同字节流。所以我们也使用id作为了文件名


**commit文件**

每一次提交都会存储为commit文件，commit文件中包含了必要的提交时间，提交信息，父节点id，以及版本中的各个文件所对应的blob。

```java
public class Commit implements Serializable {
    private String id;
    private String message;
    private Timestamp timestamp;
    private List<String> parents;
    private HashMap<String, String> blobMap;
}
```

- id:所谓的id，事实上就是将commit的message和timestamp进行了哈希处理。 
- message: 提交的信息 由`java gitlet.Main commit [commit meesgae]` 提供
- timestamp: 时间戳，采用的java.sql包下的timestamp, 因为我觉得这个输出的结果更好看一点.
- parents: 父commit id, 这里用的是List, 是因为考虑到未来合并分支后一个commit可能有两个父commit.
- blobMap: 这个版本中的每个文件的fileName和其对应的blob的blob id.

Commit implements Serializable 是为了能够在文件夹中序列化写入commit文件

commit文件以id作为文件名(因为hash值唯一) 存储在了`.gitlet/objects/commits/id`

**blobs**

blob是存取文件内容的文件. 当修改文件的内容后, 会创建新的blob文件, 每个commit文件都会存储其版本每个文件所对应的内容blob的blob id.
```java
public class Blob implements Serializable{
    private String content;
    private String id;
    private File filePath;
}
```
- content: 文件内容
- id: content的hash
- filePath: 这里存了一下filePath是为了读取和写入的好写, path事实上就是`.gitlet/objects/blobs/id`

**Branch**

分支事实上是一个pointer, 指向的是这个分支最后的commit

所以我们创建了Pointer类
```java
public class Pointer implements Serializable {
    public String next;

    public Pointer(String id) {
        next = id;
    }
}
```
Branch类继承了Pointer类，同时Branch类有自己的branchName
```java
public class Branch extends Pointer {
    private final String branchName;
}
```

**HEAD**

HEAD指向的是你当前所在的分支的最新提交, 同时包含了该分支的名字

```java
public class Head extends Pointer{
    private final String branchName;
}
```


### Main

在Main.java中, 其实很简短，就是通过命令行args判断参数的类型以调用不同的命令

### Init

init命令`java gitlet.Main init`的作用是初始化仓库

所谓的初始化，需要完成以下任务

- 如果已经存在 .gitlet，抛异常
- 创建各个文件
- 创建初始化Commit
- 创建初始化 master分支 -- 指向最新的commit 即 init commit
- 创建 HEAD -- 指向最新的commit  即 init commit
- 存储 commit, master branch, HEAD.

文件目录
```
gitlet (folder)
    |── objects (folder)
        |-- commits (folder)
        |-- blobs (folder)
    |── refs (folder)
        |── heads (folder)
            |-- master (file)
            |-- other file      
        |-- HEAD (file)
    |-- addstage (folder)   
    |-- removestage (folder)
```

![gitlet init1.png](note%2Fattachment%2Fgitlet%20init1.png)

### add
已完成
![gitlet add1.png](note%2Fattachment%2Fgitlet%20add1.png)

### commit
已完成
![gitlet commit1.png](note%2Fattachment%2Fgitlet%20commit1.png)

### rm
已完成

### status
已完成

### log
已完成

### branch
已完成

### rm-branch
已完成



