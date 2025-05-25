# 获取当前脚本所在的绝对路径，并回到项目的根目录
SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")

# 切换到项目根目录
cd "$PROJECT_ROOT"

rm -rf gitlet-test
mkdir gitlet-test

javac -d gitlet-test gitlet/*.java
cd gitlet-test

java gitlet.Main init

echo "Hello" > Hello.txt

java gitlet.Main add Hello.txt

java gitlet.Main status

java gitlet.Main commit "create Hello.txt"

java gitlet.Main log

java gitlet.Main branch main

java gitlet.Main checkout main

java gitlet.Main status

echo "aaa" > aaa.txt
echo "Hello World" > Hello.txt
cat Hello.txt
java gitlet.Main add Hello.txt
java gitlet.Main add aaa.txt
java gitlet.Main commit "create aaa.txt and update Hello.txt"
java gitlet.Main log

java gitlet.Main checkout master
echo "aaa" > aaa.txt
java gitlet.Main add aaa.txt
java gitlet.Main commit "create aaa.txt"

java gitlet.Main merge main
java gitlet.Main status
java gitlet.Main log
cat Hello.txt



