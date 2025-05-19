# Gitlet

**Author**: QingZhiLiangCheng

**Contributors**: QingZhiLiangCheng, ChengShi

**Since**: 2025-05-10

---

## Overview

这是针对伯克利大学CS61B, spring2021 Project2构建的Gitlet.

Gitlet是一个版本管理系统，仿照主流系统Git的功能并实现了其部分基本命令，包括`init`,`add`,`commit`,`rm`,`checkout`,`branch`,`reset`,`rm-branch`,`merge`
等. 官方并没有提供实质性的框架， 而是要自己设计具体使用哪些类，使用哪些数据结构.

### Features

- **提交机制**：保存文件目录的快照，以后可以恢复。
- **分支管理**：在单独的分支中维护提交序列。
- **版本切换**：将单个文件或整个分支恢复到特定提交时的状态。
- **合并功能**：合并来自不同分支的更改。
- **日志历史**：查看提交历史。
- **数据持久性**：利用 Java 序列化来持久保存数据，模拟存储库的平面目录结构。
- **错误处理**：使用 Java 的异常机制实现健壮的系统，以确保稳定性和可靠性。

### GetStart

在运行Gitlet之前, 确保在根目录下运行下面的shell命令. 将会在根目录下建立一个classes文件夹 然后将所有`.java`
文件通过javac编译成`.class`文件并存入classes文件夹

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

blob是存取文件内容的文件. 当修改文件的内容后, 会创建新的blob文件, 每个commit文件都会存储其版本每个文件所对应的内容blob的blob
id.

```java
public class Blob implements Serializable {
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
public class Head extends Pointer {
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
- 创建 HEAD -- 指向最新的commit 即 init commit
- 存储 commit, master branch, HEAD.

文件目录

```
gitlet (folder)
    |── objects (folder)
        |-- commits (folder)
            |-- <hash> <-- init commit
        |-- blobs (folder)
    |── refs (folder)
        |── heads (folder)
            |-- master (file) <-- 默认生成master分支
            |-- other file      
        |-- HEAD (folder)
             |-- HEAD (file) <-- 保存HEAD指针的对应commit id
    |-- addstage (folder)   
    |-- removestage (folder)
```

![gitlet init1.png](note%2Fattachment%2Fgitlet%20init1.png)

### add

`java gitlet.Main add [file name]`

将文件加入add暂存区

- 文件名是空 抛异常
- 工作目录中不存在此文件 抛异常
- 如果remove区中存在: 从remove暂存区移除
- 如果add区中存在: 从add暂存区移除
- 如果文件已经被track:
    - 与最新提交中内容一致: 不需要纳入add暂存区
    - 与最新提交中内容不一致: 纳入add暂存区

![gitlet add1.png](note%2Fattachment%2Fgitlet%20add1.png)

```
.gitlet (folder)
    |── objects (folder) 
        |-- commits (folder)
        |-- blobs (folder)
            |-- <hash>  <----- 加入的file.txt文件内容
    |── refs (folder)
        |── heads (folder) 
            |-- master (file)
            |-- other file     
        |-- HEAD (folder)     
            |-- HEAD (file)
    |-- addstage (folder)       
        |-- file.txt  <----- 保存blob文件的id
    |-- removestage (folder)

file.txt  <----- 加入的文件
```

### commit

`java gitlet.Main commit [commit message]`

- add stage, remove stage为空 -- "No changes added to the commit."
- commit message 为空 -- "Please enter a commit message."
- 获取old commit的blob map 因为里面要存这个版本所有的文件的位置(包括之前已经commit过的文件）
- 根据add stage, remove stage更新新的blob map
- 创建新的提交 并暂存
- 删除add stage, remove stage中的文件指针

![gitlet commit1.png](note%2Fattachment%2Fgitlet%20commit1.png)

```
.gitlet (folder)
    |── objects (folder) 
        |-- commits (folder)
            | -- <hash> <----- 添加进的commit文件
        |-- blobs (folder)
            |-- <hash>  
    |── refs (folder)
        |── heads (folder) 
            |-- master (file)
            |-- other file     
        |-- HEAD (folder)     
            |-- HEAD (file)   
    |-- addstage (folder)   <----- 将add stage的文件加入commit
    |-- removestage (folder) <---- 将remove stage从commit移除
```

### rm

`java gitlet.Main rm [file name]`

- 文件名是空 "Please enter a file name."
- 在addStage中不存在 在commit中不存在 - "No reason to remove the file."
- 在addStage中存在 -- 删除
- 在commit中存在 -- 加入remove stage
- 如果用户还没有手动删除这个文件 从工作目录中删除文件

```
.gitlet (folder)
    |── objects (folder) 
        |-- commits (folder) 
            | -- <hash> 
        |-- blobs (folder) 
            |-- <hash>  
    |── refs (folder)
        |── heads (folder) 
            |-- master (file)
            |-- other file     
       |-- HEAD (folder)     
            |-- HEAD (file)   
    |-- addstage (folder)       <----- 若是在addstage中有则删除
    |-- removestage (folder)
        |-- file.txt  <----- 添加
file.txt  <----- 若是在被track状态，则进行删除；若不是在track，就不能删除
```

### status

`java gitlet.Main status`

- Branches: 显示当前存在的分支 并用*标记当前分支
- Staged Files: 显示已暂存待添加的文件
- Remove Files: 显示已暂存待删除的文件
- 条目按照字典序排序

```bash
 === Branches ===
 *master
 other-branch
 === Staged Files ===
 wug.txt
 wug2.txt
 === Removed Files ===
 goodbye.txt
 
```

### log

`java gitlet.Main log`

这里值得注意的是, 官方文档说"Starting at the current head commit, display information about each commit backwards along
the commit tree until the initial commit, following the first parent commit links, ignoring any second parents found in
merge commits. "

所以说对于普通的提交，顺着父提交打印就行

对于合并提交：需要显示该提交的信息，包括两个父提交的简短哈希值，但仅沿着第一个父提交继续向上回溯历史。

我们最终打印出来的时间戳跟官网文档中的并不一样，因为我们使用的是java.sql的包

```bash
commit 310c73b74c44429beba493c4acdc520bf0448e7b
Date: 2025-05-18 21:59:23.279
Message: commit Hello.txt and aaa.txt
Files: Hello.txt aaa.txt

===
commit cf1284a045bdb52ab00bd47da90e5f16e048ce38
Date: 1970-01-01 08:00:00.0
Message: initial commit

```

### branch

`java gitlet.Main branch [branch name]`

创建一个指定名称的新分支，并让它指向当前的HEAD提交

但是值得注意的是，这个命令不会立即切换到新创建的分支（就像真实的 Git 一样），直到`java gitlet.Main checkout branchName`
才会切换了分支

```
.gitlet (folder)
    |── objects (folder) 
        |-- commits (folder) 
            | -- <hash> 
        |-- blobs (folder) 
            |-- <hash>  
    |── refs (folder)
        |── heads (folder) 
            |-- master (file)
            |-- other file     <----- 指向当前头部提交
        |-- HEAD (folder)     
            |-- HEAD (file)        
    |-- addstage (folder)       
    |-- removestage (folder)
file.txt  
```

### rm-branch

`java gitlet.Main rm-branch [branch name]`

删除指定名称的分支, 但这仅仅意味着删除与该分支相关联的指针；并不会删除在该分支下创建的所有提交等内容

```
.gitlet (folder)
    |── objects (folder) 
        |-- commits (folder) 
            | -- <hash> 
        |-- blobs (folder) 
            |-- <hash>  
    |── refs (folder)
        |── heads (folder) 
            |-- master (file)
            |-- other file     <----- 将此文件删除
        |-- HEAD (folder)     
            |-- HEAD (file)        
    |-- addstage (folder)       
    |-- removestage (folder)
file.txt  
```

### checkout




