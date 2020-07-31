package com.arthur.mychat.tcp.multi;

import com.arthur.mychat.core.config.ThreadPool;
import com.arthur.mychat.core.interfaces.CallBack;
import com.arthur.mychat.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PipedInputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

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
    //客户端套接字
    private Socket socket;
    private Thread read;
    private Thread send;

    private MultiClient(){}

    public MultiClient(ThreadPool threadPool){
        this.threadPool = threadPool;
    }

    public void startClient(PipedInputStream in, CallBack callBack){
        try {
            //客户端连接服务器端，返回套接字Socket对象
            socket = new Socket(Constants.SERVER_HOST,Constants.CHAT_PORT);
            //创建读取服务器端信息的线程和发送服务器端信息的线程
            read = new Thread(new ClientReadThread(socket,callBack));
            send = new Thread(new ClientSendThread(socket,in));
            //启动线程
            threadPool.execute(read);
            threadPool.execute(send);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            if(socket!=null){
                socket.close();
                socket = null;
            }
            logger.info("关闭流");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        ThreadPool thread = new ThreadPool();
        for (int i=0;i<20;i++){
            MultiClient client = new MultiClient(thread);
//            client.startClient(System.in);
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                System.out.println("退出");
                this.cancel();
            }
        }, 5000);// 这里百毫秒
    }
}
