package com.itcast.netty.c2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/9 21:08
 * @Modified By:
 */
@Slf4j
public class EventLoopServer {

    public static void main(String[] args) {
        // 增加一个独立的 EventLoopGroup    DefaultEventLoopGroup 不能处理IO事件
        EventLoopGroup group = new DefaultEventLoopGroup();
        new ServerBootstrap()
            // boss 只负责 ServerSocketChannel 上的 accept事件， worker 负责SocketChannel上的读写
            .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline()
                        .addLast("handler1", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            log.info(buf.toString(StandardCharsets.UTF_8));
                            // 消息传递给下一个 handler
                            ctx.fireChannelRead(msg);
                        }})
                        .addLast(group, "handler2", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            log.info(buf.toString(StandardCharsets.UTF_8));
                        }
                    });
                }
            })
            .bind(8080);
    }

}
