package com.arthur.mychat.core.chat;

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
    public ClientReadThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        //获取服务器端输入流
        Scanner scanner;
        try {
            scanner = new Scanner(socket.getInputStream());
            while(scanner.hasNext()){
                logger.info("客户端接收:"+scanner.next());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
