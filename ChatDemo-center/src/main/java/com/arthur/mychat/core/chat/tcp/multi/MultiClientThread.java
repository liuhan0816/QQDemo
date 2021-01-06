package com.arthur.mychat.core.chat.tcp.multi;

import com.arthur.mychat.core.gui.ClientGui;

/**
 * @ClassName MultiClientThread
 * @Author liuhan
 * @Date 2020/5/15 16:25
 */
public class MultiClientThread implements Runnable {
    private String name;

    public MultiClientThread(String name){
        this.name = name;
    }
    @Override
    public void run() {
        ClientGui clientGui = new ClientGui(name);
    }
}
