package com.arthur.mychat.core.chat.tcp.multi;

import com.arthur.mychat.core.config.BaseThread;
import com.arthur.mychat.core.config.ThreadPool;
import com.arthur.mychat.core.interfaces.CallBack;
import com.arthur.mychat.model.ext.User;
import com.arthur.mychat.util.Constants;
import com.arthur.mychat.util.SocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private HashMap<String,User> userlist = new HashMap<String,User>();
    //服务器公共对象-持有类
    private HandSocket handle;
    //线程池
    private ThreadPool threadPool;
    private CallBack serverCallBack;

    private MultiServer(){
    }

    public MultiServer(ThreadPool threadPool, CallBack serverCallBack){
        try {
            this.serverSocket = new ServerSocket(Constants.CHAT_PORT);
            this.threadPool = threadPool;
            this.serverCallBack = serverCallBack;
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
        serverCallBack.receive("启动ChatServer");
        logger.info("启动ChatServer");

        handle = new HandSocket(serverSocket, userlist);

        int count = 0;
        //循环创建线程
        while (true) {
            //连入的用户数+1
            count++;
            try {
                // 监听,等待连接
                Socket conn = serverSocket.accept();
                String userName = conn.getLocalAddress().getHostName() + ":" + conn.getPort();
                serverCallBack.receive("监听到新的客户端" + userName);
                logger.info("监听到新的客户端" + userName);
                //创建该连接对应用户所有消息存储
                ArrayList<String> userinforlist = new ArrayList<String>();
                //创建用户
                User user = new User(conn, userinforlist, count);
                user.setName(userName);
                //添加用户
                this.userlist.put(user.getName(),user);
                //为用户创建线程
                ServerThread thread = new ServerThread(handle, user);
                //线程运行
                threadPool.execute(thread);

                //检测正常连接的用户数
                //临时表暂存当前所有记录用户
                HashMap<String,User> online = new HashMap<>(userlist);
                for (Map.Entry<String, User> entry : online.entrySet()) {
                    User one = entry.getValue();

                    boolean closed = SocketUtil.isServerClose(one.getSocket());;
                    if(closed){
                        String key = entry.getKey();
                        userlist.remove(key);
                    }
                }

                serverCallBack.receive("当前在线人数" + this.userlist.size());
                logger.info("当前在线人数" + this.userlist.size());
            } catch (SocketException e){
                serverCallBack.receive("服务器端口已关闭");
                logger.error("服务器端口已关闭");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void close(){
        try {
            for (int i=0;i<this.userlist.size();i++){
                User user = this.userlist.get(i);
                if(user != null){
                    Socket conn = user.getSocket();
                    if(conn!=null){
                        conn.close();
                        conn=null;
                    }
                }
            }
            if(serverSocket !=null){
                //关闭服务器
                serverSocket.close();
                serverSocket = null;
            }
            super.close();
            serverCallBack.receive("关闭服务器");
            logger.info("关闭服务器");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //启动服务器,clicent--cmd telnet 127.0.0.1 6666来测试
    public static void main(String[] args){
        try{
            //1.创建服务器端的ServerSocket对象，等待客户端连接
            ServerSocket serverSocket = new ServerSocket(6666);
            //2.创建线程池，从而可以处理多个客户端
            ThreadPool threadPool = new ThreadPool();
            HashMap<String,User> userlist = new HashMap<String,User>();
            HandSocket handle = new HandSocket(serverSocket,userlist);
            int count=0;

            for(int i=0;i<20;i++){
                System.out.println("欢迎来到我的聊天室……");
                //3.监听客户端
                Socket conn = serverSocket.accept();
                System.out.println("有新的朋友加入……");
                count++;
//                //启动线程
//                executorServie.execute(new Server(socket));

                //创建该连接对应用户所有消息存储
                ArrayList<String> userinforlist = new ArrayList<String>();
                //创建用户
                User user = new User(conn, userinforlist, count);
                String name=conn.getLocalAddress().getAddress().toString();
                user.setName(name);
                //添加用户

                userlist.put(user.getName(),user);
                //为用户创建线程
                ServerThread thread = new ServerThread(handle, user);
                //线程运行
                threadPool.execute(thread);
                //thread.run();

                logger.info("当前在线人数" + userlist.size());
            }
            //关闭线程池
            threadPool.clearAll();
            //关闭服务器
            serverSocket.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
