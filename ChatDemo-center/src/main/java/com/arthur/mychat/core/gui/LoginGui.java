package com.arthur.mychat.core.gui;

import com.arthur.mychat.core.config.ThreadPool;
import com.arthur.mychat.core.swing.ImagePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @ClassName LoginGui
 * @Author liuhan
 * @Date 2020/7/31 14:46
 */
public class LoginGui extends JFrame implements ActionListener{
    private static final Logger logger = LoggerFactory.getLogger(LoginGui.class);
    private JButton[] buttons;
    private String[] btnName;
    private JPanel[] panels;
    //加载图片
    ImageIcon icon=new ImageIcon(this.getClass().getResource(
            "/static/images/login.jpg"));

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void init(){
        this.setTitle("登录界面");          // 创建窗口
        this.setSize(1024, 800);                       // 设置窗口大小
        this.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // 当点击窗口的关闭按钮时,仅关闭自身

        this.setIconImage(icon.getImage());
        initSwing();

//        this.pack();
        this.setVisible(true);
    }

    public void initSwing(){
        Font font=new Font("宋体",Font.PLAIN,18);



        panels = new JPanel[3];
        panels[0] = new JPanel();
        panels[0].add(new JLabel("账户"));
        panels[0].add(new JTextField(10));

        panels[1] = new JPanel();
        panels[1].add(new JLabel("密码"));
        panels[1].add(new JPasswordField(10));

        buttons = new JButton[2];
        btnName = new String[2];
        btnName[0]="关闭";
        btnName[1]="发送";

        buttons[0] = new JButton(btnName[0]);
        buttons[0].setFont(font);
        buttons[0].addActionListener(this);

        buttons[1] = new JButton(btnName[1]);
        buttons[1].setFont(font);
        buttons[1].addActionListener(this);

        panels[2] = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panels[2].add(buttons[0]);
        panels[2].add(buttons[1]);

        Box vBox = Box.createVerticalBox();
        vBox.add(panels[0]);
        vBox.add(panels[1]);
        vBox.add(panels[2]);


        ImagePanel imagePanel = new ImagePanel(this, icon);
//        JLayeredPane  layeredPane= new JLayeredPane ();
//        layeredPane.add(imagePanel , new Integer(100));
//        layeredPane.add(vBox, new Integer(200));

//        addImageByRepaint(icon);
        this.setContentPane(imagePanel);
    }

    public void addImageByRepaint(ImageIcon imageIcon) {
        ImagePanel imagePanel = new ImagePanel(this, imageIcon);
        setContentPane(imagePanel);

    }


    public static void main(String[] args) {
        //服务端线程池
        ThreadPool threadPool = new ThreadPool();

        LoginGui loginGui= new LoginGui();
        loginGui.init();
    }
}
