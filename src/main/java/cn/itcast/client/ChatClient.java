package cn.itcast.client;

import cn.itcast.protocol.MessageCodecSharable;
import cn.itcast.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/24 22:30
 * @Modified By:
 */
@Slf4j
public class ChatClient {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();

        /** 线程间可共享的 handler */
        LoggingHandler logHandler = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    /** 解决粘包、半包，使用 LengthFieldBasedFrameDecoder 解码器*/
                    pipeline.addLast(new ProtocolFrameDecoder());
                    pipeline.addLast(logHandler);
                    pipeline.addLast(messageCodec);
                }
            });
            Channel channel = bootstrap.connect("127.0.0.1", 8080).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }

}
