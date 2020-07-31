package com.arthur.mychat.core.swing;

import com.arthur.mychat.core.chat.tcp.multi.MultiServer;
import com.arthur.mychat.core.config.ThreadPool;
import com.arthur.mychat.core.interfaces.CallBack;
import com.arthur.mychat.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author liuhan
 * @date 2019/12/30 16:51
 */
public class ServerGui extends JFrame implements ActionListener , CallBack {
    private static final Logger logger = LoggerFactory.getLogger(ServerGui.class);

    private MultiServer s;
    private ThreadPool threadPool;

    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    //GUI组件
    private JTextArea area;
    private JScrollPane scroll;
    private JToggleButton[] buttons;
    private String[] btnName;

    private ServerGui(){
    }
    public ServerGui(ThreadPool threadPool){
        this.threadPool = threadPool;
        initServer();
    }

    public void initServer()  {
        if(s == null){
            s = new MultiServer(threadPool,this);
        }
    }
    /**
     * 绘制swing组件
     * @description
     * @return:
     * @author: liuhan
     * @date: 2019/12/30 16:18
     */
    public void drawSwing(){
        String title = "Java QQ 服务端:";
        try{
            title += InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.setTitle(title);
        this.setSize(1024, 800);                       // 设置窗口大小
        this.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）

        Font font1=new Font("宋体",Font.BOLD,20);
        // 创建选项卡面板
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(font1);

        // 创建第 1 个选项卡（选项卡只包含 标题）
        tabbedPane.addTab("系统服务", new ImageIcon("/images/interfaces.jpg"), createGridBagPanel("TAB 01"));

        // 添加选项卡选中状态改变的监听器
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("当前选中的选项卡: " + tabbedPane.getSelectedIndex());
            }
        });

        // 设置默认选中的选项卡
        tabbedPane.setSelectedIndex(0);

        this.setContentPane(tabbedPane);
        this.setVisible(true);
    }

    /**
     * 创建一个面板，面板中心显示一个标签，用于表示某个选项卡需要显示的内容
     */
    private JComponent createGridBagPanel(String text) {


        //字体设置
        Font font1=new Font("宋体",Font.BOLD,20);
        Font font2=new Font("宋体",Font.PLAIN,18);

        Component box1 =Box.createGlue();
        box1.setVisible(true);
        Component box2 =Box.createGlue();
        box2.setVisible(true);

        buttons = new JToggleButton[2];
        btnName = new String[2];
        btnName[0] = "启动Java QQ服务";
        btnName[1] = "终止Java QQ服务";
        buttons[0]=new JToggleButton(btnName[0]);
        buttons[1]=new JToggleButton(btnName[1]);
        buttons[0].setPreferredSize(new Dimension(200,50));
        buttons[0].setFont(font1);


        buttons[1].setPreferredSize(new Dimension(200,50));
        buttons[1].setFont(font1);


        JTextField textField = new JTextField();
        textField.setBackground(Color.WHITE);
        textField.setFont(font2);

        area = new JTextArea(20,40);
        area.setFont(font2);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //按钮事件
        buttons[0].addActionListener(this);
        buttons[1].addActionListener(this);

        /* 添加 组件 和 约束 到 布局管理器 */
        GridBagLayout gridBag = new GridBagLayout();// 布局管理器
        GridBagConstraints c = null;// 约束

        c = new GridBagConstraints();
        //组件显示区域 水平/竖直方向 所占单元格的个数
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        //如何 分布额外空间（，指定行/列中的其中 任意一个 组件的权重（大于0），默认为0。
        c.weightx=1;
        c.weighty=0.5;
        //组件的外部填充（可看做是 组件的外边距，也可看做是 显示区域 的内边距），即 组件 与其 显示区域边缘 之间间距的最小量
        c.insets=new Insets(10, 10, 10, 10);
        gridBag.addLayoutComponent(box1, c);

        // Button01
        c = new GridBagConstraints();
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx=1;
        c.weighty=0.5;
        c.insets=new Insets(10, 10, 10, 10);
        gridBag.addLayoutComponent(buttons[0], c); // 内部使用的仅是 c 的副本

        // Button02 显示区域占满当前行剩余空间（换行），组件填充显示区域
        c = new GridBagConstraints();
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx=1;
        c.weighty=0.5;
        c.insets=new Insets(10, 10, 10, 10);
        gridBag.addLayoutComponent(buttons[1], c);

        c = new GridBagConstraints();
        //占完所在行/列余下所有单元格（该值可实现 换行 作用）
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=1;
        c.weighty=0.5;
        c.insets=new Insets(10, 10, 10, 10);
        gridBag.addLayoutComponent(box2, c);

        // textField 显示区域占满当前行剩余空间（换行），组件填充显示区域
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx=1;
        c.weighty=1;
        c.insets=new Insets(10, 10, 10, 10);
        gridBag.addLayoutComponent(textField, c);

        // area 显示区域占满当前行剩余空间（换行），组件填充显示区域
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 6;
        c.fill = GridBagConstraints.BOTH;
        c.weightx=1;
        c.weighty=1;
        c.insets=new Insets(10, 10, 10, 10);
        gridBag.addLayoutComponent(scroll, c);


        JPanel panel = new JPanel(gridBag);
        /* 添加 组件 到 内容面板 */
        panel.add(box1);
        panel.add(buttons[0]);
        panel.add(buttons[1]);
        panel.add(box2);
        panel.add(textField);
        panel.add(scroll);
        panel.setFont(font1);
        return panel;
    }

    /**
     * 按钮事件实现
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(btnName[0])){
            //启动聊天服务器
            initServer();
            threadPool.execute(s);
            buttons[0].setSelected(true);
            buttons[1].setSelected(false);
            area.append(df.format(LocalDateTime.now())+"-"+"启动聊天服务器" + Constants.LINE_BREAK);
        }else if(e.getActionCommand().equals(btnName[1])){
            if(s!=null){
                s.close();
                logger.info("关闭GUI");
                s=null;
            }else{
                area.append("请勿重复关闭");
            }
            //终止聊天服务器
//                threadPool.clearAll();
            buttons[0].setSelected(false);
            buttons[1].setSelected(true);
            area.append(df.format(LocalDateTime.now())+"-"+"终止聊天服务器" + Constants.LINE_BREAK);
        }
    }

    public static void main(String[] args) {
        //服务端线程池
        ThreadPool threadPool = new ThreadPool();

        ServerGui serverGui= null;
        serverGui = new ServerGui(threadPool);
        serverGui.drawSwing();
    }

    @Override
    public void receive(String msg) {
        area.append(msg + Constants.LINE_BREAK);
    }
}
