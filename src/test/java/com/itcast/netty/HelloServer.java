package com.itcast.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/7 21:59
 * @Modified By:
 */
public class HelloServer {

    public static void main(String[] args) {
        // 1.启动器，负责组装netty组件，启动服务器
        new ServerBootstrap()
            //2.包含 selector、thread, 负责处理事件，事件触发后，调用 handler
            .group(new NioEventLoopGroup())
            //3.选择服务器的 ServerSocketChannel 的实现  OIO -- BIO
            .channel(NioServerSocketChannel.class)
            //4.负责处理读写   ChannelInitializer - 建立连接后初始化
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    // 添加handler
                    ch.pipeline().addLast(new StringDecoder());  //解码器 - 将byteBuf 转为字符串
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            System.out.println(msg);;
                        }
                    });
                }
            })
            .bind(8080);
    }

}
