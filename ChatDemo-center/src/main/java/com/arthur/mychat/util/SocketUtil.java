package com.arthur.mychat.util;

import java.net.Socket;

/**
 * @ClassName SocketUtil
 * @Author liuhan
 * @Date 2020/7/31 11:09
 */
public class SocketUtil {
    /**
     * 判断接口是否关闭
     * @param socket
     * @return
     */
    public static Boolean isServerClose(Socket socket){
        try{
            //发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            socket.sendUrgentData(0xFF);
            return false;
        }catch(Exception se){
            return true;
        }
    }
}
