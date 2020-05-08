package com.arthur.mychat.core.swing;

import com.arthur.mychat.core.chat.MultiClient;
import com.arthur.mychat.core.config.ThreadPool;
import com.arthur.mychat.util.Constants;
import com.arthur.mychat.util.InOutStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.One;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * @author liuhan
 * @date 2020/1/9 15:34
 */
public class ChatClientGui {
    private static final Logger logger = LoggerFactory.getLogger(ChatClientGui.class);
    private ThreadPool threadPool;
    private MultiClient client;
    private ByteArrayOutputStream os;

    private ChatClientGui(){
    }

    public ChatClientGui(ThreadPool threadPool){
        this.threadPool = threadPool;
        initClient();
    }

    private void initClient(){
        try {
            if (client == null) {
                this.client = new MultiClient(threadPool);
                os = new ByteArrayOutputStream();
//                InputStream in = InOutStream.parse(os);
                InputStream in = new ByteArrayInputStream(os.toByteArray());
                this.client.startClient(Constants.CHAT_PORT, in);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("输入输出流转换出错");
        }
    }


    /**
     * 初始化选项卡面板
     * @param text
     */
    public void createClientGui(String text){
        // 1. 创建一个顶层容器（窗口）
        JFrame jf = new JFrame("Java QQ 客户端:"+text);          // 创建窗口
        jf.setSize(1024, 800);                       // 设置窗口大小
        jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // 当点击窗口的关闭按钮时,仅关闭自身

        Font font=new Font("宋体",Font.PLAIN,18);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("+", new JLabel());
        tabbedPane.addTab("Hello", createChatTab(tabbedPane));

        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

        tabbedPane.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                if(tabbedPane.indexOfTab("+")==tabbedPane.getSelectedIndex()){
                    Component c = createChatTab(tabbedPane);
                    tabbedPane.add("标签名",c);
                    tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
                }
            }
        });
        tabbedPane.setFont(font);

        tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
        jf.setContentPane(tabbedPane);
        jf.setVisible(true);
    }

    /**
     * 初始化聊天tab
     * @param tabbedPane
     * @return
     */
    public JComponent createChatTab(JTabbedPane tabbedPane){
        Font font=new Font("宋体",Font.PLAIN,18);
        //初始化swing组件

        JTextArea area01 = new JTextArea(20,40);
        area01.setEditable(false);
        area01.setFont(font);

        JTextArea area02 = new JTextArea(5,40);
        area02.setFont(font);

        JButton btn01 = new JButton("关闭");
        btn01.setFont(font);
        btn01.addActionListener(e->{
            tabbedPane.remove(tabbedPane.getSelectedIndex());
        });

        JButton btn02 = new JButton("发送");
        btn02.setFont(font);
        btn02.addActionListener(e->{
            try{
                String msg = area02.getText();
                if(StringUtils.isNoneEmpty(msg)){
                    area02.setText("");
                    os.write(msg.getBytes());
                    os.flush();
                }
            }catch (Exception except){
                except.printStackTrace();
            }
        });

        JPanel panel_btn = new JPanel(new FlowLayout(FlowLayout.RIGHT,20,40));
        panel_btn.add(btn01);
        panel_btn.add(btn02);

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
        gridBag.addLayoutComponent(area01, c);

        // textField 显示区域占满当前行剩余空间（换行），组件填充显示区域
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx=1;
        c.weighty=1;
        c.insets=new Insets(10, 10, 10, 10);
        gridBag.addLayoutComponent(area02, c);

        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx=1;
        c.weighty=0.5;
        c.insets=new Insets(10, 10, 10, 10);
        gridBag.addLayoutComponent(panel_btn, c); // 内部使用的仅是 c 的副本


        JPanel panel = new JPanel(gridBag);
        /* 添加 组件 到 内容面板 */
        panel.add(area01);
        panel.add(area02);
        panel.add(panel_btn);

        return panel;
    }
}
