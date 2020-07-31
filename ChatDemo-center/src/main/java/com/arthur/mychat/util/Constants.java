package com.arthur.mychat.util;

/**
 * 固定配置
 * @author liuhan
 * @date 2019/12/30 9:46
 */
public class Constants {
    //默认字符串编码UTF-8
    final public static String CHARSET_NAME = "UTF-8";
    //服务器主机地址
    final public static String SERVER_HOST = "127.0.0.1";
    //服务器端口号
    final public static int CHAT_PORT = 6666;
    //换行符
    final public static String LINE_BREAK = "\r\n";
    //输入流结束标志符
    final public static String INPUT_STREAM_END_FLAG = "INPUT_STREAM_END";

    //线程相关定量
    final public static Integer CORE_THREAD_MAX = 1000;
    final public static String THREAD_FLAG_START = "START";
    final public static String THREAD_FLAG_END = "END";

}
