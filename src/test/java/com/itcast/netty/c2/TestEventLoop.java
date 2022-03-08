package com.itcast.netty.c2;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/8 21:50
 * @Modified By:
 */
@Slf4j
public class TestEventLoop {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup(2);  // 可以处理：IO事件、普通任务、定时任务
//        EventLoopGroup group = new DefaultEventLoopGroup();  // 可以处理：IO事件、普通任务、定时任务

        System.out.println(NettyRuntime.availableProcessors());

        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        //普通任务
        group.next().submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("ok");
        });

        log.info("main");

        //定时任务
        group.next().scheduleAtFixedRate(() ->{
            log.info("ok2");
        }, 1L, 1L, TimeUnit.SECONDS);

        log.info("main2");
    }

}
