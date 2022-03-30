package cn.itcast.client;

import cn.hutool.json.JSONUtil;
import cn.itcast.message.*;
import cn.itcast.protocol.MessageCodecSharable;
import cn.itcast.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;


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

        CountDownLatch wait_for_login = new CountDownLatch(1);
        AtomicBoolean login = new AtomicBoolean();
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
//                    pipeline.addLast(logHandler);
                    pipeline.addLast(messageCodec);
                    pipeline.addLast("client handler", new ChannelInboundHandlerAdapter() {
                        /** 连接建立后触发 active 事件 */
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            // 用户线程
                            new Thread(() -> {
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("请输入用户名");
                                String username = scanner.nextLine().trim();
                                System.out.println("请输入密码");
                                String pw = scanner.nextLine().trim();
                                LoginRequestMessage message = new LoginRequestMessage(username, pw);
                                /** 从当前处理器向前查找 OutboundHandler 处理器执行 */
                                ctx.writeAndFlush(message);

                                System.out.println("等待输入：");
                                try {
                                    wait_for_login.await();
                                    // 登录失败
                                    if (!login.get()) {
                                        ctx.channel().close();
                                        return;
                                    }
                                    while (true) {
                                        System.out.println("==================================");
                                        System.out.println("send [username] [content]");
                                        System.out.println("gsend [group name] [content]");
                                        System.out.println("gcreate [group name] [m1,m2,m3...]");
                                        System.out.println("gmembers [group name]");
                                        System.out.println("gjoin [group name]");
                                        System.out.println("gquit [group name]");
                                        System.out.println("quit");
                                        System.out.println("==================================");
                                        String command = scanner.nextLine();
                                        String[] s = command.split(" ");
                                        switch (s[0]) {
                                            case "send":
                                                ctx.writeAndFlush(new ChatRequestMessage(username, s[1], s[2]));
                                                break;
                                            case "gsend":
                                                ctx.writeAndFlush(new GroupChatRequestMessage(username, s[1], s[2]));
                                                break;
                                            case "gcreate":
                                                HashSet<String> set = new HashSet<>(Arrays.asList(s[2].split(",")));
                                                set.add(username);
                                                ctx.writeAndFlush(new GroupCreateRequestMessage(s[1], set));
                                                break;
                                            case "gmembers":
                                                ctx.writeAndFlush(new GroupMembersRequestMessage(s[1]));
                                                break;
                                            case "gjoin":
                                                ctx.writeAndFlush(new GroupJoinRequestMessage(username, s[1]));
                                                break;
                                            case "gquit":
                                                ctx.writeAndFlush(new GroupQuitRequestMessage(username, s[1]));
                                                break;
                                            case "quit":
                                                ctx.channel().close();
                                                break;
                                            default:
                                                System.out.println("指令错误");
                                                break;
                                        }

                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }, "system in").start();
                        }

                        /** 接收响应消息 */
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.info("msg: {}", JSONUtil.toJsonStr(msg));
                            if (msg instanceof LoginResponseMessage){
                                LoginResponseMessage message = (LoginResponseMessage) msg;
                                if (message.isSuccess()) login.compareAndSet(false, true);
                                wait_for_login.countDown();
                            }
                        }
                    });
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
