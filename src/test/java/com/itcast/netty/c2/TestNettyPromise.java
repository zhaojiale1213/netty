package com.itcast.netty.c2;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/12 15:55
 * @Modified By:
 */
@Slf4j
public class TestNettyPromise {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup();

        DefaultPromise<Integer> promise = new DefaultPromise<>(group.next());

        new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("开始计算");
                try {
                    TimeUnit.SECONDS.sleep(2L);
                    int i = 1/0;
                    promise.setSuccess(20);
                } catch (Exception e) {
                    promise.setFailure(e);
                }

            }
        }).start();

        log.info("等待结果");
        log.info("结果：{}", promise.get());

    }

}
