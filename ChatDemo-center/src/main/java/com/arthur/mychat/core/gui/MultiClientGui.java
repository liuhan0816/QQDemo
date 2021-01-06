package com.arthur.mychat.core.gui;

import com.arthur.mychat.core.chat.tcp.multi.MultiClientThread;
import com.arthur.mychat.core.config.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * @author liuhan
 * @date 2020/1/9 15:34
 */
public class MultiClientGui extends JFrame implements ActionListener , WindowListener {
    private static final Logger logger = LoggerFactory.getLogger(MultiClientGui.class);
    private ThreadPool threadPool;
    //GUI组件
    private JButton[] buttons;
    private String[] btnName;
    private JTextField textField;
    private ArrayList<ClientGui> clientList = new ArrayList<ClientGui>();

    private MultiClientGui(){
    }

    public MultiClientGui(ThreadPool threadPool){
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
        this.setTitle("Java QQ 客户端");
        this.setSize(300, 200);                       // 设置窗口大小
        this.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）

        Font font=new Font("宋体",Font.PLAIN,18);
        // 创建内容面板，指定使用 流式布局
        JPanel panel = new JPanel(new FlowLayout());

        textField = new JTextField(20);
        textField.setFont(font);
        panel.add(textField);

        buttons = new JButton[1];
        btnName = new String[1];
        btnName[0] = "生成随机客户端";

        buttons[0] = new JButton(btnName[0]);


        buttons[0].setFont(font);
        buttons[0].addActionListener(this);

        panel.add(textField);
        panel.add(buttons[0]);

        this.setContentPane(panel);
        this.setVisible(true);
    }
    public void close(){
     /*   for (int i=0;i<clientList.size();i++){
            clientList.get(i).close();
        }*/
        threadPool.clearAll();
        logger.info("关闭Mulit-GUI");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(btnName[0])){
            String name = textField.getText();
//            ClientGui clientGui = new ClientGui(name);
//            clientList.add(clientGui);
            threadPool.execute(new MultiClientThread(name));
            logger.info("生成随机客户端"+name);
        }
    }

    public static void main(String[] args) {
        //客户端线程池
        ThreadPool threadPool = new ThreadPool();

        MultiClientGui clientGui=new MultiClientGui(threadPool);
        clientGui.drawSwing();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
