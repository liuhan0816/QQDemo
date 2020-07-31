package com.arthur.mychat.core.chat.udp;

import java.io.IOException;

/**
 * @ClassName Test
 * @Author liuhan
 * @Date 2020/5/11 10:55
 */
public class Test {
    public static void main(String[] args) throws IOException, InterruptedException {
        //开启一条线程，监听端口、接收消息
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Udp.receive();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        receiveThread.start();

        //怕接收线程暂时没分配到时间片，在发送消息之后才执行，没能接收到掐前面的消息，可以让发送消息的线程沉睡会儿
        Thread.sleep(100);

        //也可以单独开启一条线程来发送消息
        Udp.send("127.0.0.1","hello,How are you?");
    }
}
