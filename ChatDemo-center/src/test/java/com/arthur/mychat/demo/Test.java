package com.arthur.mychat.demo;

import com.arthur.mychat.core.chat.MultiClient;
import com.arthur.mychat.core.config.ThreadPool;
import com.arthur.mychat.core.swing.ChatServerGui;
import com.arthur.mychat.util.Constants;

import java.io.IOException;

/**
 * @author liuhan
 * @date 2019/12/30 16:12
 */
public class Test {
    /**
     * @description 创建服务器Gui,让服务器开始对话工作
     * @return:
     * @author: liuhan
     * @date: 2019/12/30 16:18
     */
    @org.junit.Test
    public void startChatServerGui() throws IOException {
        ThreadPool threadPool = new ThreadPool();
        ChatServerGui serverGui=new ChatServerGui(threadPool);
        serverGui.drawSwing();
    }

    /**
     * @description 创建客户端Gui
     * @return: void
     * @author: liuhan
     * @date: 2020/1/9 15:32
     */
    @org.junit.Test
    public void startChatClientGui(){
        MultiClient client = new MultiClient(new ThreadPool());
        client.startClient(Constants.CHAT_PORT, System.in);
    }


    public static void main(String[] args) {
        Test test =new Test();
        try {
            test.startChatServerGui();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        test.startChatClientGui();
    }
}
