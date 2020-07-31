package com.arthur.mychat.tcp.multi;

import com.arthur.mychat.model.ext.User;
import com.arthur.mychat.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 服务器公共对象-持有类
 * @ClassName HandSocket
 * @author liuhan
 * @date 2019/12/27 17:15
 */

public class HandSocket {
    private static final Logger logger = LoggerFactory.getLogger(HandSocket.class);
    //当前服务器
    private ServerSocket socket;
    //当前客户端
    private Socket thissocket;
    //所有客户端
    private HashMap<String,User> userlist = new HashMap<String,User>();
    //当前用户
    private User user;

    public HandSocket(ServerSocket socket, Socket thissocket, HashMap<String,User> userlist, User user){

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
                    out.write(Constants.LINE_BREAK.getBytes(Constants.CHARSET_NAME));
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

    /**
     * 用户上线注册
     * @param userName
     * @param user
     */
    public void userRegist(String userName,User user){
        userlist.put(userName,user);
        logger.info("[用户名为"+userName+"][客户端为"+user.getSocket()+"]上线了！");
        logger.info("当前在线人数为："+userlist.size()+"人");
    }

    /**
     * 用户下线注销
     * @param user
     */
    public void userExit(User user){
        //利用socket取得对应的key值
        String userName = user.getName();

        //将userName,user元素从map集合中删除
        userlist.remove(userName,user);
        //提醒服务器客户端已下线
        logger.info("用户："+userName+"已下线");
    }

    /**
     * 私聊发送
     * @param user
     * @param userName 私聊对象用户名
     * @param msg
     * @throws IOException
     */
    public void privateChat(User user,String userName,String msg) throws IOException{
        //取得当前客户端的用户名
        String curUser = user.getName();

        //取得私聊用户名对应的客户端
        Socket client = userlist.get(userName).getSocket();
        //获取私聊客户端的输出流，将私聊信息发送到指定客户端
        PrintStream printStream = new PrintStream(client.getOutputStream());
        printStream.println(curUser+"私聊说:"+msg);
    }

    /**
     * 群聊发送
     * @param user
     * @param msg
     * @throws IOException
     */
    public void groupChat(User user,String msg) throws IOException{
        //将Map集合转换为Set集合
        Set<Map.Entry<String,User>> set = userlist.entrySet();
        //遍历Set集合找到发起群聊信息的用户
        String userName = user.getName();

        //遍历Set集合将群聊信息发给每一个客户端
        for(Map.Entry<String,User> entry:set){
            String  toUserName = entry.getKey();
            String content = "";
            if(userName.equals(toUserName)){
                content = "你群聊说:"+msg;
            }else{
                content = userName+"群聊说:"+msg;
            }
            //取得客户端的Socket对象
            Socket client = entry.getValue().getSocket();
            //取得client客户端的输出流
            PrintStream printStream = new PrintStream(client.getOutputStream());
            printStream.println(content);
        }
    }
}
