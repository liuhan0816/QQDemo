package com.arthur.mychat.core.interfaces;

/**客户端组件回写接收内容
 * @ClassName CallBack
 * @Author liuhan
 * @Date 2020/5/12 9:18
 */
public interface CallBack {
    //客户端接收内容,写入客户端Swing
    public void receive(String msg);
}
