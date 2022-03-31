package cn.itcast.server;

import cn.itcast.protocol.MessageCodecSharable;
import cn.itcast.protocol.ProtocolFrameDecoder;
import cn.itcast.server.handler.ChatRequestMessageHandler;
import cn.itcast.server.handler.GroupChatRequestMessageHandler;
import cn.itcast.server.handler.GroupCreateRequestMessageHandler;
import cn.itcast.server.handler.LoginRequestMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/23 21:33
 * @Modified By:
 */
@Slf4j
public class ChatServer {

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        /** 线程间可共享的 handler */
        LoggingHandler logHandler = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        LoginRequestMessageHandler loginHandler = new LoginRequestMessageHandler();
        ChatRequestMessageHandler chatHandler = new ChatRequestMessageHandler();
        GroupCreateRequestMessageHandler groupCreateHandler = new GroupCreateRequestMessageHandler();
        GroupChatRequestMessageHandler groupChatHandler = new GroupChatRequestMessageHandler();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    /** 解决粘包、半包，使用 LengthFieldBasedFrameDecoder 解码器*/
                    pipeline.addLast(new ProtocolFrameDecoder());
//                    pipeline.addLast(logHandler);
                    pipeline.addLast(messageCodec);
                    pipeline.addLast(loginHandler);
                    pipeline.addLast(chatHandler);
                    pipeline.addLast(groupCreateHandler);
                    pipeline.addLast(groupChatHandler);
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("sever err", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
