package com.arthur.mychat.core.config;

import com.arthur.mychat.util.Constants;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author liuhan
 * @date 2020/1/9 13:35
 */
public class ThreadPool {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPool.class);
    //线程池
    @Getter
    @Setter
    private ExecutorService threadPool;

    public ThreadPool(){
        //设置核心线程数
        threadPool = Executors.newFixedThreadPool(Constants.CORE_THREAD_MAX);
    }

    /**
     * @description 清理线程池所有线程
     * @return:
     * @author: liuhan
     * @date: 2020/1/9 13:50
     */
    public void clearAll() {
        List<Runnable> list = threadPool.shutdownNow();
        for (int i = 0; i < list.size(); i++) {
            BaseThread one = (BaseThread) list.get(i);
            one.close();
        }
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                logger.info("Pool did not terminate");
            }
        }catch(Exception e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public void execute(Runnable runnable){
        threadPool.execute(runnable);
    }
}
