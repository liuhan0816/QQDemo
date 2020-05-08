package com.arthur.mychat.demo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @ClassName ChatTwoClient
 * @Author liuhan
 * @Date 2020/4/28 16:45
 */
public class ChatTwoClient extends JFrame implements ActionListener {
    private String name;
    private JTextArea text_re;
    private JTextField text_se;
    private PrintWriter cout;
    private JButton buttons[];
    public ChatTwoClient(String name,Socket socket) throws IOException
    {
        super("我:"+name+ InetAddress.getLocalHost().getHostAddress()+":"+socket.getLocalPort());
        this.setBounds(320, 240, 400, 240);
        this.text_re=new JTextArea();
        this.text_re.setEditable(false);
        this.getContentPane().add(new JScrollPane(this.text_re));

        JToolBar toolBar=new JToolBar();
        this.getContentPane().add(toolBar,"South");
        toolBar.add(this.text_se=new JTextField(30));
        buttons=new JButton[2];
        buttons[0]=new JButton("发送");
        buttons[1]=new JButton("下线");
        toolBar.add(buttons[0]);
        toolBar.add(buttons[1]);
        buttons[0].addActionListener(this);
        buttons[1].addActionListener(this);//给按钮添加事件监听，委托当前对象处理
        this.setVisible(true);
        this.name=name;
        this.cout=new PrintWriter(socket.getOutputStream(),true);//获得socket输出流
        this.cout.println(name);
        BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream())); //将socket的字节输入流转换为字符流，默认GBK字符集，再创建缓冲字符输入流
        String line="连接"+br.readLine()+"成功";
        while(line!=null&&!line.endsWith("bye"))
        {
            text_re.append(line+"\r\n");
            line=br.readLine();
        }//读取对方发送的内容并显示，直到内容为为空或对方下线
        br.close();
        this.cout.close();
        socket.close();
        buttons[0].setEnabled(false);
        buttons[1].setEnabled(false);
    }
    public ChatTwoClient(String name,String host,int port) throws IOException
    {
        this(name,new Socket(host,port));//调用另一个构造方法
    }
    public void actionPerformed(ActionEvent ev)
    {
        if(ev.getActionCommand().equals("发送"))
        {
            this.cout.println(name+":"+text_se.getText());
            text_re.append("我："+text_se.getText()+"\n");
            text_se.setText("");
        }//按下发送按钮后，将内容发出，并更新自己聊天框的内容
        if(ev.getActionCommand().equals("下线"))
        {
            text_re.append("你已下线\n");
            this.cout.println(name+"离线\n"+"bye\n");
            buttons[0].setEnabled(false);
            buttons[1].setEnabled(false);
        }//下线按钮按下后，发送bye作为下线标记
    }


    public static void main(String[] args) throws IOException {
        new ChatTwoClient("mxl","127.0.0.1",6666); //ip地址和端口
    }
}
