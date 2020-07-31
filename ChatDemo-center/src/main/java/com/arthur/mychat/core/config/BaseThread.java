package com.arthur.mychat.core.config;

import com.arthur.mychat.util.Constants;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuhan
 * @date 2020/1/9 13:39
 */
public class BaseThread {
    private static final Logger logger = LoggerFactory.getLogger(BaseThread.class);

    @Getter
    @Setter
    //线程状态
    public String status = Constants.THREAD_FLAG_START;

    /**
     * @description 终止线程
     * @return:
     * @author: liuhan
     * @date: 2020/1/9 11:00
     */
    private void termination(){
        this.status = Constants.THREAD_FLAG_END;
    }

    /**
     * 是否停止了线程
     * @return
     */
    public boolean isTermination(){
        if(Constants.THREAD_FLAG_END.equals(this.status)){
            logger.info("线程被终止了");
            return true;
        }else{
            return false;
        }
    }

    public void close(){
        this.termination();
    }
}
