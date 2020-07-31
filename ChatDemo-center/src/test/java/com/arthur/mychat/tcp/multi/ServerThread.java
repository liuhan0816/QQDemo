package com.arthur.mychat.tcp.multi;

import com.arthur.mychat.core.config.BaseThread;
import com.arthur.mychat.model.ext.User;
import com.arthur.mychat.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * 服务器线程类--用户连上服务器时提供相应服务
 * @ClassName ServerThread
 * @author liuhan
 * @date 2019/12/27 17:13
 */
public class ServerThread extends BaseThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Thread.class);

    //服务器
    private ServerSocket serversocket;

    //所有客户端信息
    private HashMap<String,User> userlist;
    //当前客户端
    private User user;
    //线程状态
    private String status;
    //
    private HandSocket handle;

    public ServerThread(ServerSocket serversocket, User user, HashMap<String,User> userlist) {
        //服务器
        this.serversocket=serversocket;
        //当前客户端所有信息
        this.user=user;
        //所有用户端
        this.userlist=userlist;

        status = Constants.THREAD_FLAG_START;
    }


    @Override
    public void run() {
        //当前用户的连接
        Socket conn = user.getSocket();

        handle= new HandSocket(serversocket, conn, userlist,user);
        logger.info("启动用户线程"+conn.getLocalAddress().toString()+conn.getPort());
        try {
            InputStream in = conn.getInputStream();
            OutputStream out = conn.getOutputStream();
//            BufferedInputStream bis = new BufferedInputStream(in);
//            byte[] buf = new byte[1024];
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,Constants.CHARSET_NAME));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,Constants.CHARSET_NAME));
            //用户连上服务器--写入欢迎消息
            writer.write("欢迎" + user.getName() + "加入聊天室" + Constants.LINE_BREAK);
            writer.flush();

            while (true) {
                if(isTermination()){
                    return;
                }
                String msg = reader.readLine();
                if(StringUtils.isNoneEmpty(msg)){
                    //服务器写入消息
                    writer.write(("you said:"));
                    writer.write(msg);
                    writer.write(Constants.LINE_BREAK);
                    //发出群聊消息
                    handle.groupChat(user,msg);
                    //用户信息
                    user.save(msg);//把消息存入该用户输入的所有信息中
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(){
        try {
            Socket conn = this.user.getSocket();
            if(conn!=null){
                conn.close();
            }
            logger.info("关闭流");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            super.close();
        }
    }
}
