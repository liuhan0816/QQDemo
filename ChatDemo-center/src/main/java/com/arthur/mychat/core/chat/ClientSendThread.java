package com.arthur.mychat.core.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 客户端发送类
 * @ClassName ClientSendThread
 * @Author liuhan
 * @Date 2020/1/9 15:59
 */
public class ClientSendThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ClientSendThread.class);
    private Socket socket;
    private InputStream in;
    public ClientSendThread(Socket socket,InputStream in){
        this.socket = socket;
        this.in = in;
    }


    @Override
    public void run(){
        try {
            //获取服务器端的输出流
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            //客服端的输入流
            Scanner scanner = new Scanner(in);
            while(true){
                String msg = null;
                if(scanner.hasNext()){
                    msg = scanner.next();
                    printStream.println(msg);
                }
                if("exit".equals(msg)){
                    scanner.close();
                    printStream.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
