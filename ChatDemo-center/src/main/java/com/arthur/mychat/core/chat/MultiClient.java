package com.arthur.mychat.core.chat;

import com.arthur.mychat.core.config.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 *
 * @ClassName MultiClient
 * @Author liuhan
 * @Date 2020/1/9 16:04
 */
public class MultiClient {
    private static final Logger logger = LoggerFactory.getLogger(MultiClient.class);
    //线程池
    private ThreadPool threadPool;
    //客户端端口
    Socket socket;

    private MultiClient(){}

    public MultiClient(ThreadPool threadPool){
        this.threadPool = threadPool;
    }

    public void startClient(int port, InputStream in){
        try {
            //客户端连接服务器端，返回套接字Socket对象
            socket = new Socket("127.0.0.1",port);
            //创建读取服务器端信息的线程和发送服务器端信息的线程
            Thread read = new Thread(new ClientReadThread(socket));
            Thread send = new Thread(new ClientSendThread(socket,in));
            //启动线程
            threadPool.execute(read);
            threadPool.execute(send);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
