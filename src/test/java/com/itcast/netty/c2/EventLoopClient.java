package com.itcast.netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/9 21:08
 * @Modified By:
 */
@Slf4j
public class EventLoopClient {

    public static void main(String[] args) throws InterruptedException {
        //1.启动类
        // Future、Promise 类型异步使用
        ChannelFuture channelFuture = new Bootstrap()
            //2.添加 EventLoopGroup
            .group(new NioEventLoopGroup())
            //3.客户端 SocketChannel 实现
            .channel(NioSocketChannel.class)
            //4.添加处理器
            .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override  // 初始化连接 - 连接建立后调用
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new StringEncoder());
                }
            })
            // 异步非阻塞的方法 main线程发起调用，nio线程（NioEventLoopGroup 中的一个线程）去执行
            .connect(new InetSocketAddress("127.0.0.1", 8080));

        // 1.使用 sync方法同步处理结果
//        channelFuture.sync();  // 同步  阻塞方法 - 等待连接建立
//        Channel channel = channelFuture.channel();// 连接对象
//        log.info("{}", channel);
//        System.out.println();

        //2. 使用 addListener方法(回调)异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            //nio线程在连接建立后，会调用 operationComplete 方法  --  回调方法
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                log.info("{}", channel);
            }
        });
    }
}
