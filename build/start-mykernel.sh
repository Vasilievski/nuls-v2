#!/bin/bash

help()
{
    cat <<- EOF
    Desc: 启动NULS 2.0钱包，
    Usage: ./start.sh
    		-c <module.json> 使用指定配置文件 如果不配置将使用./default-config.json
    		-f 前台运行
    		-l <logs path> 输出的日志目录
    		-d <data path> 数据存储目录
    		-j JAVA_HOME
    		-D debug模式，在logs目录下输出名为stdut.log的全日志文件
    		-h help
    Author: zlj
EOF
    exit 0
}
APP_NAME="mykernel"
cd `dirname $0`;
BIN_PATH=`pwd`
if [ -d ../Libraries/JAVA/11.0.2 ]; then
    JAVA_HOME=`dirname "../Libraries/JAVA/11.0.2/bin"`;
    JAVA_HOME=`cd $JAVA_HOME; pwd`
    JAVA="${JAVA_HOME}/bin/java"
else
    JAVA='java'
fi
JAVA_EXIST=`${JAVA} -version 2>&1 |grep 11`
if [ ! -n "$JAVA_EXIST" ]; then
    echo "JDK version is not 11"
    ${JAVA} -version
    exit 0;
fi
echo "JAVA_HOME:${JAVA_HOME}"
echo `${JAVA} -version`
function get_fullpath()
{
    if [ -f "$1" ];
    then
        tempDir=`dirname $1`;
        echo `cd $tempDir; pwd`;
        else
        echo `cd $1; pwd`;
    fi
}

checkIsRunning(){
    if [ ! -z "`ps -ef|grep -w "name=${APP_NAME} "|grep -v grep|awk '{print $2}'`" ]; then
        pid=`ps -ef|grep -w "name=${APP_NAME} "|grep -v grep|awk '{print $2}'`
        echo "$APP_NAME Already running pid=$pid";
        exit 0;
    fi
}

checkIsRunning

RUNFRONT=
while getopts fj:c:l:d:Dh name
do
            case $name in
            f)     RUNFRONT="1";;
            j)     JAVA_HOME="$OPTARG";;
            c)     
                    CONFIG="`get_fullpath $OPTARG`/${OPTARG##*/}"
#                    if [ "${CONFIG##*.}"x != "properties"x ]; then
#                        echo "-c setting config file must be *.ncf"
#                        exit 1;
#                    fi
                    ;;
            l)
                   if [ ! -d "$OPTARG" ]; then
                       mkdir $OPTARG
                       if [ ! -d "$OPTARG" ]; then
                          echo "$OPTARG not a folder"
                       exit 0 ;
                       fi
                   fi
                   LOGPATH="`get_fullpath $OPTARG`";;
            d)
                   if [ ! -d "$OPTARG" ]; then
                       mkdir $OPTARG
                       if [ ! -d "$OPTARG" ]; then
                          echo "$OPTARG not a folder"
                       exit 0 ;
                       fi
                   fi
                   DATAPATH="`get_fullpath $OPTARG`";;
            D)     DEBUG="1";;
            h)     help ;;
            ?)     exit 2;;
           esac
done
if [ ! -f "$CONFIG" ]; then
    CONFIG="${BIN_PATH}/default-config.json"
fi
if [ ! -n "$LOGPATH" ];
then
    LOGPATH="`get_fullpath ../logs`"
fi
if [ ! -d "$LOGPATH" ]; then
   mkdir $LOGPATH
fi

if [ -n "$DATAPATH" ];
then
    DATAPATH="-DDataPath=${DATAPATH}"
else
    if [ ! -d ../data ]; then
        mkdir ../data
    fi
    DATAPATH="-DDataPath=`get_fullpath ../data`"
fi
echo "log path : ${LOGPATH}"
echo "data path : ${DATAPATH}"

cd ../Modules/Nuls
MODULE_PATH=`pwd`
if [ -n "${RUNFRONT}" ];
then
    ${JAVA} -server -Ddebug="${DEBUG}" -Dapp.name=mykernel -Dlog.path="${LOGPATH}" ${DATAPATH} -Dactive.module="$CONFIG" -classpath ./libs/*:./mykernel/1.0.0/mykernel-1.0.0.jar io.nuls.mykernel.MyKernelBootstrap startModule $MODULE_PATH $CONFIG
else
    nohup ${JAVA} -server -Ddebug="${DEBUG}" -Dapp.name=mykernel -Dlog.path="${LOGPATH}" ${DATAPATH}  -Dactive.module="$CONFIG"  -classpath ./libs/*:./mykernel/1.0.0/mykernel-1.0.0.jar io.nuls.mykernel.MyKernelBootstrap startModule $MODULE_PATH $CONFIG > "${LOGPATH}/stdut.log" 2>&1 &
fi


