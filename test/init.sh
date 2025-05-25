# 获取当前脚本所在的绝对路径，并回到项目的根目录
SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")

# 切换到项目根目录
cd "$PROJECT_ROOT"

rm -rf gitlet-test
mkdir gitlet-test

javac -d gitlet-test gitlet/*.java
cd gitlet-test

echo "java gitlet.Main init"
java gitlet.Main init
echo

echo "java gitlet.Main log"
java gitlet.Main log




