package com.arthur.mychat.core.chat;

import com.arthur.mychat.model.ext.User;
import com.arthur.mychat.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * @ClassName Handsocket
 * @author liuhan
 * @date 2019/12/27 17:15
 */

public class Handsocket {
    private static final Logger logger = LoggerFactory.getLogger(Handsocket.class);
    //当前服务器
    private ServerSocket socket;
    //当前客户端
    private Socket thissocket;
    //所有客户端
    private ArrayList<User> userlist;
    //当前用户
    private User user;

    public Handsocket(ServerSocket socket,Socket thissocket,ArrayList<User> userlist,User user){

        this.socket=socket;
        this.thissocket=thissocket;
        this.userlist=userlist;
        this.user=user;
    }

    //群发消息
    public void send_to_all(String s){

        for(int i=0;i<userlist.size();i++){
            if(userlist.get(i)!=user){
                try{
                    OutputStream out = userlist.get(i).socket.getOutputStream();
                    String nnum = ""+user.num;
                    out.write(nnum.getBytes(Constants.CHARSET_NAME));
                    out.write((" said:" + s).getBytes(Constants.CHARSET_NAME));
                    out.write("\r\n".getBytes(Constants.CHARSET_NAME));
                }
                catch(SocketException e){
                    e.printStackTrace();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }
}
