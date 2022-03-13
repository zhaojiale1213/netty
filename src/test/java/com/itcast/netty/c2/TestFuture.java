package com.itcast.netty.c2;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/12 15:23
 * @Modified By:
 */
@Slf4j
public class TestFuture {


    /**
     * JDK-Future
     */
    @Test
    public void test() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);

        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                TimeUnit.SECONDS.sleep(2L);
                return new Random().nextInt(10);
            }
        });

        System.out.println("结果："+ future.get());
    }


    /**
     * Netty-Future
     */
    @Test
    public void test1() throws ExecutionException, InterruptedException {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup();
        EventLoop eventLoop = loopGroup.next();

        io.netty.util.concurrent.Future<Integer> future = eventLoop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                TimeUnit.SECONDS.sleep(2L);
                return new Random().nextInt(10);
            }
        });
        log.info("开始计算");
        // 同步获取
//        System.out.println(future.get());

        //异步获取 - 线程执行完后回调此方法  --  operationComplete
        future.addListener(new GenericFutureListener<io.netty.util.concurrent.Future<? super Integer>>() {
            @Override
            public void operationComplete(io.netty.util.concurrent.Future<? super Integer> future) throws Exception {
                log.debug("结果：{}", future.get());
                log.info("结果：{}", future.get());
            }
        });
    }

}
