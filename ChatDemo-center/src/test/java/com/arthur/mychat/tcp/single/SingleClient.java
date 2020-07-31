package com.arthur.mychat.tcp.single;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @ClassName SingleClient
 * @Author liuhan
 * @Date 2020/5/9 17:02
 */
public class SingleClient {
    public static void main(String[] args) throws IOException {
        //1.客户端连接服务器端，返回套接字Socket对象
        Socket socket = new Socket("127.0.0.1",6666);
        //2.获取服务器端的输出流，向服务器端输出内容
        PrintStream printStream = new PrintStream(socket.getOutputStream());
        printStream.println("我是客户端"+socket.getLocalPort());
        //3.获取服务器端的输入流，读取服务器端的内容
        Scanner scanner = new Scanner(socket.getInputStream());
        scanner.useDelimiter("\n");
        if(scanner.hasNext()){
            System.out.println(scanner.next());
        }
        //4.关闭流
        socket.close();
    }
}
