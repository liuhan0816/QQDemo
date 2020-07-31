package com.arthur.mychat.tcp.multi;

import com.arthur.mychat.core.interfaces.CallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @ClassName ClientReadThread
 * @Author liuhan
 * @Date 2020/1/9 15:55
 */
public class ClientReadThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ClientReadThread.class);
    private Socket socket;
    private CallBack clientCallBack;
    private Scanner scanner;

    public ClientReadThread(Socket socket, CallBack clientCallBack){
        this.socket = socket;
        this.clientCallBack = clientCallBack;
    }

    @Override
    public void run(){
        //获取服务器端输入流
        try {
            scanner = new Scanner(socket.getInputStream());
            while(scanner!=null && scanner.hasNext()){
                String text = scanner.next();
                clientCallBack.receive(text);
                logger.info("客户端接收:"+ text);
            }
            close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        if(scanner!=null){
            scanner.close();
            scanner = null;
        }
        logger.info("关闭流");
    }
}
