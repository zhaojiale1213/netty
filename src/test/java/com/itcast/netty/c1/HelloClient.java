package com.itcast.netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/7 22:32
 * @Modified By:
 */
public class HelloClient {

    public static void main(String[] args) throws InterruptedException {
        //1.启动类
        new Bootstrap()
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
            .connect(new InetSocketAddress("127.0.0.1", 8080))
            .sync()  // 同步  阻塞方法 - 等待连接建立
            .channel() // 连接对象
            .writeAndFlush("hello world"); // 发送数据  -  StringEncoder（handler）编码

    }

}
