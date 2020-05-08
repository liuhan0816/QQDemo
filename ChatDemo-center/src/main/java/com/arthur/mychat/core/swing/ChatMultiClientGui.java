package com.arthur.mychat.core.swing;

import com.arthur.mychat.core.chat.MultiClient;
import com.arthur.mychat.core.config.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @author liuhan
 * @date 2020/1/9 15:34
 */
public class ChatMultiClientGui {
    private static final Logger logger = LoggerFactory.getLogger(ChatMultiClientGui.class);
    private ThreadPool threadPool;

    private ChatMultiClientGui(){
    }

    public ChatMultiClientGui(ThreadPool threadPool){
        this.threadPool = threadPool;
    }

    /**
     * 绘制swing组件
     * @description
     * @return:
     * @author: liuhan
     * @date: 2019/12/30 16:18
     */
    public void drawSwing(){
        // 1. 创建一个顶层容器（窗口）
        JFrame jf = new JFrame("Java QQ 客户端");          // 创建窗口
        jf.setSize(1024, 800);                       // 设置窗口大小
        jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）

        Font font=new Font("宋体",Font.PLAIN,18);
        // 创建内容面板，指定使用 流式布局
        JPanel panel = new JPanel(new FlowLayout());

        JTextField textField = new JTextField(20);
        textField.setFont(font);
        panel.add(textField);

        JButton btn01 = new JButton("生成随机客户端");
        btn01.setFont(font);
        btn01.addActionListener(e -> {
            String text = textField.getText();
            ChatClientGui clientGui = new ChatClientGui(threadPool);
            clientGui.createClientGui(text);
            logger.info("生成随机客户端"+text);
        });

        panel.add(textField);
        panel.add(btn01);

        jf.setContentPane(panel);
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool();

        ChatMultiClientGui clientGui=new ChatMultiClientGui(threadPool);
        clientGui.drawSwing();
    }
}
