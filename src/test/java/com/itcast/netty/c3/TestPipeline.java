package com.itcast.netty.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/12 19:49
 * @Modified By:
 */
@Slf4j
public class TestPipeline {

    public static void main(String[] args) {
        new ServerBootstrap()
            .group(new NioEventLoopGroup())
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    /** 处理器顺序： head -> h1 -> h2 -> h3 -> h4 -> h5 -> h6 -> tail 双向链表 */
                    /** OutboundHandler 反向执行 h6  h5  h4 */
                    pipeline.addLast("h1", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.info("1");
                            ByteBuf buf = (ByteBuf) msg;
                            String s = buf.toString(Charset.defaultCharset());
                            /** 将数据传递给下一个 InboundHandler 处理器，不调用执行链结束，不再往下执行 */
                            super.channelRead(ctx, s);
                        }
                    });
                    pipeline.addLast("h2", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.info("2");
                            Student student = new Student(msg.toString());
                            /** 将数据传递给下一个 InboundHandler 处理器 */
                            super.channelRead(ctx, student);
                        }
                    });
                    pipeline.addLast("h3", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.info("3, 结果：{}, class: {}", msg, msg.getClass());
                            log.info("ch: {},  ctx.channel: {}", ch, ctx.channel());
                            /** 从tail处理器往前查找 OutboundHandler 处理器执行  都是向前查找*/
                            ch.writeAndFlush(ctx.alloc().buffer().writeBytes("server ....".getBytes()));

                            /** 从当前处理器向前查找 OutboundHandler 处理器执行 */
//                            ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("server ....".getBytes()));

                        }
                    });

                    pipeline.addLast("h4", new ChannelOutboundHandlerAdapter() {
                        @Override
                        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                            log.info("4");
                            super.write(ctx, msg, promise);
                        }
                    });
                    pipeline.addLast("h5", new ChannelOutboundHandlerAdapter() {
                        @Override
                        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                            log.info("5");
                            super.write(ctx, msg, promise);
                        }
                    });
                    pipeline.addLast("h6", new ChannelOutboundHandlerAdapter() {
                        @Override
                        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                            log.info("6");
                            super.write(ctx, msg, promise);
                        }
                    });
                }
            })
            .bind(8080);
    }

    @Data
    @AllArgsConstructor
    static class Student {
        private String name;
    }

}
