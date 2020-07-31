package com.arthur.mychat.core.swing;

import com.arthur.mychat.core.interfaces.CallBack;
import com.arthur.mychat.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author liuhan
 * @date 2020/1/9 15:34
 */
public class ClientGui extends JFrame implements ActionListener, WindowListener, CallBack {
    private static final Logger logger = LoggerFactory.getLogger(ClientGui.class);
    //swing
    private JTextArea[] textAreas;
    private JButton[] buttons;
    private String[] btnName;
    private JPanel panelBtn;
    //流
    private Socket socket;
    private PrintWriter cout;
    private BufferedReader br;
    private String name;

    public ClientGui(String name){
        this.name = name;
        createClientGui();

        initConnect();
    }

    private void initConnect(){
        try {

            //客户端连接服务器端，返回套接字Socket对象
            socket = new Socket(Constants.SERVER_HOST,Constants.CHAT_PORT);
            this.cout=new PrintWriter(socket.getOutputStream(),true);//获得socket输出流
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line=br.readLine();
            while(line!=null&&!line.endsWith(Constants.INPUT_STREAM_END_FLAG)){
                this.receive(line + Constants.LINE_BREAK);
                line=br.readLine();
            }//读取对方发送的内容并显示，直到内容为为空或对方下线

            close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 初始化选项卡面板
     */
    public void createClientGui(){
        // 1. 创建一个顶层容器（窗口）
        this.setTitle("Java QQ 客户端:"+name);          // 创建窗口
        this.setSize(1024, 800);                       // 设置窗口大小
        this.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // 当点击窗口的关闭按钮时,仅关闭自身
        this.addWindowListener(this);

        initSwing();
        initLayout();

        this.setVisible(true);


    }

    /**
     * 初始化swing组件
     */
    public void initSwing(){
        //初始化swing组件
        Font font=new Font("宋体",Font.PLAIN,18);

        textAreas = new JTextArea[2];
        textAreas[0] = new JTextArea(20,40);
        textAreas[0].setEditable(false);
        textAreas[0].setFont(font);

        textAreas[1] = new JTextArea(5,40);
        textAreas[1].setFont(font);

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

        panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT,20,40));
        panelBtn.add(buttons[0]);
        panelBtn.add(buttons[1]);
    }

    /**
     * 初始化布局
     * @return
     */
    public void initLayout(){

        /* 添加 组件 和 约束 到 布局管理器 */
        GridBagLayout gridBag = new GridBagLayout();// 布局管理器
        GridBagConstraints c = null;// 约束

        // area 显示区域占满当前行剩余空间（换行），组件填充显示区域
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 6;
        c.fill = GridBagConstraints.BOTH;
        c.weightx=1;
        c.weighty=1;
        c.insets=new Insets(10, 10, 10, 10);
        gridBag.addLayoutComponent(textAreas[0], c);

        // textField 显示区域占满当前行剩余空间（换行），组件填充显示区域
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx=1;
        c.weighty=1;
        c.insets=new Insets(10, 10, 10, 10);
        gridBag.addLayoutComponent(textAreas[1], c);

        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx=1;
        c.weighty=0.5;
        c.insets=new Insets(10, 10, 10, 10);
        gridBag.addLayoutComponent(panelBtn, c); // 内部使用的仅是 c 的副本


        this.setLayout(gridBag);
        this.add(textAreas[0]);
        this.add(textAreas[1]);
        this.add(panelBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(btnName[0])){
            //关闭
            close();
        }else if(e.getActionCommand().equals(btnName[1])){
            try{
                String msg = textAreas[1].getText();
                if(StringUtils.isNoneEmpty(msg)){
                    textAreas[1].setText("");
                    this.cout.println(msg);
//                    textAreas[0].append("你说:"+msg);
                }
            }catch (Exception except){
                except.printStackTrace();
            }
        }
    }


    public void close(){
        this.dispose();
        try {
           /* if(br != null){
                br.close();
                br = null;
            }
            if(cout != null){
                cout.close();
                cout = null;
            }*/
            if(socket != null){
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("关闭GUI");
    }

    /**
     * 回调接口,接收内容写入textArea
     * @param msg
     */
    @Override
    public void receive(String msg) {
        textAreas[0].append(msg + Constants.LINE_BREAK);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }
    //窗口关闭事件
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
