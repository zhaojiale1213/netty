package com.itcast.source;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/4/12 21:37
 * @Modified By:
 */
@Slf4j
public class TestConnectionTimeout {

    public static void main(String[] args) {
        // 1. 客户端通过 .option() 方法配置参数 给 SocketChannel 配置参数

        // 2. 服务端
//        new ServerBootstrap().option()  // 是给 ServerSocketChannel 配置参数  处理建立连接事件
//        new ServerBootstrap().childOption()  // 给 SocketChannel 配置参数  处理读写事件

        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                .group(group)
                /* 客户端配置超时时间, 只有客户端要配 */
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler());
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8080);
            future.sync().channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("timeout");
        } finally {
            group.shutdownGracefully();
        }
    }

}
