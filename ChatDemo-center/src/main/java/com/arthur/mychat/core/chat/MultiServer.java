package com.arthur.mychat.core.chat;

import com.arthur.mychat.core.config.BaseThread;
import com.arthur.mychat.core.config.ThreadPool;
import com.arthur.mychat.model.ext.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * 创建服务器类,之后在主函数中用该类声明该类的对象
 * @author liuhan
 * @date 2019/12/27 17:10
 */
public class MultiServer extends BaseThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(MultiServer.class);
    //一个服务器
    private ServerSocket serverSocket;

    //所有客户端
    private ArrayList<User> userlist = new ArrayList<User>();
    //线程池
    private ThreadPool threadPool;

    private MultiServer(){
    }

    public MultiServer(int port, ThreadPool threadPool){
        try {
            this.serverSocket = new ServerSocket(port);
            this.threadPool = threadPool;

            logger.info("正在初始化ChatServer");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @description 启动聊天服务器
     * @return:
     * @author: liuhan
     * @date: 2020/1/9 10:40
     */
    @Override
    public void run() {
        logger.info("启动ChatServer");
        try {
            int count = 0;
            //循环创建线程
            while (true) {
                //连入的用户数+1
                count++;
                // 监听,等待连接
                Socket conn = serverSocket.accept();

                logger.info("监听到新的客户端" + conn.getLocalAddress().toString() + conn.getPort());

                //创建该连接对应用户所有消息存储
                ArrayList<String> userinforlist = new ArrayList<String>();
                //创建用户
                User user = new User(conn, userinforlist, count);
                //添加用户
                this.userlist.add(user);
                //为用户创建线程
                ServerThread thread = new ServerThread(serverSocket, user, this.userlist);
                //线程运行
                threadPool.execute(thread);
                //thread.run();

                logger.info("当前在线人数" + this.userlist.size());
            }
        } catch (SocketException e){
            logger.error("服务器端口已关闭");
//            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(){
        super.close();
        try {
            for (int i=0;i<this.userlist.size();i++){
                Socket conn = this.userlist.get(i).getSocket();
                if(conn!=null){
                    conn.close();
                }
            }
            if(serverSocket !=null){
                //关闭服务器
                serverSocket.close();
                serverSocket = null;
            }

            logger.info("关闭服务器");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
