    #!/bin/sh  
      
    # ftail.sh = tail -f 的增强版本，可检查文件是否重建过或删除过  
    # usage: ftail.sh <file>  
    # author: codingstandards@gmail.com  
    # release time: v0.1 2010.11.04/05  
      
    # 显示title  
    echo "+---------------------------------------------------------------------------------------------+"  
    echo "| ftail.sh v0.1 - a bash script that enhanced 'tail -f', written by codingstandards@gmail.com |" >&2  
    echo "+---------------------------------------------------------------------------------------------+"  
    echo  
      
    # 判断参数个数  
    if [ "$#" != "1" ]; then  
        echo "usage: $0 <file>" >&2  
        exit 1  
    fi  
      
    # 取文件参数  
    FILE="$1"  
      
    # 取文件的inode  
    INODE=$(stat -c "%i" "$FILE")  
      
    # 启动tail -f进程，并打印信息  
    # usage: fork_tail  
    fork_tail()  
    {  
        if [ -r "$FILE" ]; then  
            tail -f "$FILE" &  
            PID=$!  
            echo "##### $0: FILE $FILE INODE=$INODE PID $PID #####" >&2  
        else  
            PID=  
            INODE=  
            echo "##### $0: FILE $FILE NOT FOUND #####" >&2  
        fi  
    }  
      
    # 杀掉tail进程  
    # usage: kill_tail  
    kill_tail()  
    {  
        if [ "$PID" ]; then  
            kill $PID  
        fi  
    }  
      
    # 检查inode是否变化了  
    # usage: inode_changed  
    inode_changed()  
    {  
        NEW_INODE=$(stat -c "%i" "$FILE" 2>/dev/null)  
        if [ "$INODE" == "$NEW_INODE" ]; then  
            return 1  
        else  
            INODE=$NEW_INODE  
        fi  
    }  
      
    # 设置陷阱，按Ctrl+C终止或者退出时杀掉tail进程  
    trap "kill_tail; exit" SIGINT SIGTERM SIGQUIT EXIT  
      
    # 首次启动tail -f进程  
    fork_tail  
      
    # 每隔一定时间检查文件的inode是否变化，如果变化就先杀掉原来的tail进程，重新启动tail进程  
    while :  
    do  
        sleep 5  
        if inode_changed; then  
            kill_tail  
            fork_tail  
        fi  
    done  
      
    # END.  
