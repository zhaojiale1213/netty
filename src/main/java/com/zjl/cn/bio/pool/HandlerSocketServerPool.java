package com.zjl.cn.bio.pool;

import java.util.concurrent.*;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/8 13:27
 * @Modified By:
 */
public class HandlerSocketServerPool {

    private static final ExecutorService executorService;

    /** 核心线程被占用后，新任务进入队列等待，队列满之后，才会创建新的线程接受任务（最大线程数） */
    static {
//        executorService = Executors.newFixedThreadPool(5);
        executorService = new ThreadPoolExecutor(2, 4, 60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(5));
    }

    public void execute(Runnable t) {
        executorService.execute(t);
    }

}
