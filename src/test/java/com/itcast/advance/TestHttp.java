package com.itcast.advance;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/17 22:36
 * @Modified By:
 */
@Slf4j
public class TestHttp {

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            ChannelFuture channelFuture = serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        /** Http协议编解码器 - codec */
                        ch.pipeline().addLast(new HttpServerCodec());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
                                log.info("method: {}, uri: {}", msg.method(), msg.uri());
                                DefaultFullHttpResponse response =
                                    new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
                                byte[] bytes = "<h1>hello, world</h1>".getBytes();
                                //表明响应长度
                                response.headers().setInt(CONTENT_LENGTH, bytes.length);
                                //设置响应内容
                                response.content().writeBytes(bytes);

                                ctx.writeAndFlush(response);
                            }
                        });
//                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//                            @Override
//                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                log.info("msg class: {}", msg.getClass());
//                                /** 请求行  请求头 */
//                                if (msg instanceof HttpRequest) {
//                                    log.info("HttpRequest msg: {}", msg.toString());
//                                }
//                                /** 请求体 -- post请求才会有 body */
//                                if (msg instanceof HttpContent) {
//                                    log.info("HttpContent msg: {}", msg.toString());
//                                }
//                            }
//                        });
                    }
                })
                .bind(8080)
                .sync();

            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
