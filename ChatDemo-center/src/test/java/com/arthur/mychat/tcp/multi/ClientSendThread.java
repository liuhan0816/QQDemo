package com.arthur.mychat.tcp.multi;

import com.arthur.mychat.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 客户端发送类
 * @ClassName ClientSendThread
 * @Author liuhan
 * @Date 2020/1/9 15:59
 */
public class ClientSendThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ClientSendThread.class);
    private Socket socket;
    private PipedInputStream in;
    private PrintStream printStream;
    private Scanner scanner;
    public ClientSendThread(Socket socket, PipedInputStream in){
        this.socket = socket;
        this.in = in;
    }


    @Override
    public void run(){
        try {
            //获取服务器端的输出流
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            //客户端的输入流

            byte[] buf = new byte[1024 * 8];
            int length = in.read(buf);
            String message = new String(buf, 0, length, "UTF-8");

            printStream.println(message + Constants.LINE_BREAK);
            close();
           /* scanner = new Scanner(in);
           String msg = null;
            while(true){
                if (scanner != null) {
                    if(scanner.hasNextLine()){
                        msg = scanner.nextLine();
                        printStream.println(msg + Constants.LINE_BREAK);
                    }
                    if("exit".equals(msg)){
                        close();
                        break;
                    }
                }else{
                    close();
                    break;
                }
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            if(in!=null){
                in.close();
                in = null;
            }
            if(scanner!=null){
                scanner.close();
                scanner = null;
            }
            if(printStream!=null){
                printStream.close();
                printStream = null;
            }
            logger.info("关闭流");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
