package com.arthur.mychat.demo;

import com.arthur.mychat.core.swing.ImagePanel;

import javax.swing.*;
import java.awt.*;

/**
 * 展示样例-GUI设置背景图像
 * @ClassName ImageFrame
 * @Author liuhan
 * @Date 2020/9/9 14:47
 */
public class ImageFrame extends JFrame {

    Dimension frameSize = new Dimension(500, 300);
    ImageIcon imageIcon = null;
    public ImageFrame() {
        imageIcon = new ImageIcon(this.getClass().getResource(
                "/static/images/login.jpg"));
        // 设置窗体属性
        setSize(frameSize);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setIconImage(imageIcon.getImage());
        //去掉默认边框关闭、最小化、最大化
        setUndecorated(true);
    }
    public void addImageByJLable() {
        setLayout(null);
        // 设置背景
        JLabel lbBg = new JLabel(imageIcon);
        lbBg.setBounds(0, 0, frameSize.width, frameSize.height);
        this.getContentPane().add(lbBg);
        addComponents();
        setVisible(true);
    }
    public void addImageByRepaint() {
        ImagePanel imagePanel = new ImagePanel(this, imageIcon);
        setContentPane(imagePanel);
        addComponents();
        setVisible(true);
    }
    private void addComponents() {
        JButton btn1 = new JButton("haha");
        btn1.setBounds(10, 20, 60, 30);
        this.getContentPane().add(btn1);
        JTextField jtf = new JTextField("22222222222");
        jtf.setBounds(200, 100, 80, 30);
        this.getContentPane().add(jtf);
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ImageFrame imageFrame = new ImageFrame();
        // imageFrame.addImageByJLable();
        imageFrame.addImageByRepaint();
    }
}
