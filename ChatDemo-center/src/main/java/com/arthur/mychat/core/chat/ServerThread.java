package com.arthur.mychat.core.chat;

import com.arthur.mychat.core.config.BaseThread;
import com.arthur.mychat.model.ext.User;
import com.arthur.mychat.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 服务器线程类
 * @ClassName ServerThread
 * @author liuhan
 * @date 2019/12/27 17:13
 */
public class ServerThread extends BaseThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Thread.class);

    //服务器
    private ServerSocket serversocket;

    //所有客户端信息
    private ArrayList<User> userlist = new  ArrayList<User>();
    //当前客户端
    private User user;
    //线程状态
    private String status;

    public ServerThread(ServerSocket serversocket, User user, ArrayList<User> userlist) {
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

        logger.info("启动用户线程"+conn.getLocalAddress().toString()+conn.getPort());
        try {
            InputStream filein = conn.getInputStream();
            OutputStream out = conn.getOutputStream();
//            BufferedInputStream bis = new BufferedInputStream(filein);
//            byte[] buf = new byte[1024];
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(filein,Constants.CHARSET_NAME));

            out.write("欢迎!\r\n".getBytes());
            out.flush();

            while (true) {
                if(isTermination()){
                    return;
                }

                Handsocket handle= new Handsocket(serversocket, conn, userlist,user);
                String guestinformation = reader.readLine();
//                String type,name,password;

                //~前为类型
                //type=guestinformation.split("~")[0];
                //System.out.println(type);
                //~和&间为名字
                //name=guestinformation.split("~")[1].split("&")[0];
                //&后面为密码
                //password=guestinformation.split("~")[1].split("&")[1];


                //waitcheck wc = new waitcheck(conn,name,password);//创建待检查成员
                //wc.checktype);
                if(StringUtils.isNoneEmpty(guestinformation)){
                    out.write(("you said:").getBytes(Constants.CHARSET_NAME));
                    out.write(guestinformation.getBytes(Constants.CHARSET_NAME));
                    out.write("\r\n".getBytes(Constants.CHARSET_NAME));

                    handle.send_to_all(guestinformation);//发出群聊消息
                    //用户信息
                    user.save(guestinformation);//把消息存入该用户输入的所有信息中
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
        super.close();
        try {
            Socket conn = this.user.getSocket();
            if(conn!=null){
                conn.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
