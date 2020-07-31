package com.arthur.mychat.core.chat.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @ClassName Udp
 * @Author liuhan
 * @Date 2020/5/11 10:53
 */
public class Udp {
    private static final int port = 9000;  //要使用的端口号

    /**
     * 发送消息
     * @Param ip 对方的ip，String
     * @Param msg 要发送的消息，String类型
     */
    public static void send(String ip,String msg) throws IOException {
        //对方的ip，不能直接用String，需要转换一下
        InetAddress ipAddr = InetAddress.getByName(ip);

        //socket,相当于码头
        DatagramSocket socket = new DatagramSocket();

        // packet，数据包，相当于集装箱
        // 参数：byte[]、数据长度、对方ip（不能直接写String）、要使用的端口号
        // 什么类型都可以传输，比如传文件，不局限于String，因为都要转换为字节数组
        DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(),ipAddr,port);

        //通过socket发送packet
        socket.send(packet);
        System.out.println("send："+msg);

        //关闭socket
        socket.close();
    }


    /**
     *监听端口，接收消息
     */
    public static void receive() throws IOException {
        //socket，指定要监听的端口。发送、接收使用的端口要相同
        DatagramSocket socket = new DatagramSocket(port);

        // packet，用一个byte[]来存储数据
        // 第一个数值是字节数组的长度，第二个数值是要读取的字节数，把读取到的数据放到byte[]中
        // 读取的字节数要小于等于byte[]的长度，不然放不下
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);

        //一直监听
        while (true){
            socket.receive(packet); //通过socket接收packet，用packet来封装接收到的数据，没接收到数据时会一直阻塞
            byte[] data = packet.getData(); //获取packet中的数据（整个byte[])
            int length = packet.getLength(); //获取数据的实际长度。packet中的byte[]不一定是装满的，需要获取实际的字节数

            String msg = new String(data, 0, length); //byte[]、offset、length
            // msg = new String(data); //其实不获取实际长度也行
            System.out.println("received："+msg);

            //获取本机ip、发送方ip
            // InetAddress localAddress = socket.getLocalAddress(); //本机
            // InetAddress address = packet.getAddress();  //发送方
            // String ip = address.getHostAddress(); //String类型的ip

            // socket.close();  //关闭socket
            //一直监听，不关闭socket
            // 退出聊天时，比如退出桌面程序或web项目点击“结束聊天”、超过2分钟未互动，需要关闭socket

        }
    }
}
