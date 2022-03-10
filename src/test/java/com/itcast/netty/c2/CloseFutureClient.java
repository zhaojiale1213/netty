package com.itcast.netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/10 22:42
 * @Modified By:
 */
@Slf4j
public class CloseFutureClient {

    public static void main(String[] args) throws InterruptedException {
        ChannelFuture future = new Bootstrap()
            .group(new NioEventLoopGroup())
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new StringEncoder());
                }
            })
            .connect(new InetSocketAddress("127.0.0.1", 8080));

        Channel channel = future.sync().channel();
        log.debug("{}", channel);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String line = scanner.nextLine();
                    if ("q".equalsIgnoreCase(line)) {
                        // close 异步操作
                        channel.close();
                        break;
                    }
                    channel.writeAndFlush(line);
                }
            }
        }, "input").start();

        /** 处理关闭  1.同步关闭  2.异步关闭*/
        ChannelFuture closeFuture = channel.closeFuture();
//        log.info("wait close");
//        closeFuture.sync();
//        log.debug("处理关闭=====");

        closeFuture.addListener(new ChannelFutureListener() {
            @Override // 调用close方法的线程，待关闭后，回调此方法
            public void operationComplete(ChannelFuture future) throws Exception {
                log.debug("处理关闭=====");
            }
        });

    }

}
