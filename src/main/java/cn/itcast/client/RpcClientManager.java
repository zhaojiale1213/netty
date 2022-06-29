package cn.itcast.client;

import cn.itcast.client.handler.RpcResponseMessageHandler;
import cn.itcast.message.RpcRequestMessage;
import cn.itcast.protocol.MessageCodecSharable;
import cn.itcast.protocol.ProtocolFrameDecoder;
import cn.itcast.protocol.SequenceIdGenerator;
import cn.itcast.server.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * @Description: 单例模式
 * @Author: zjl
 * @Date:Created in 2022/4/17 21:55
 * @Modified By:
 */
@Slf4j
public class RpcClientManager {

    private static Channel channel = null;

    private static final Object LOCK = new Object();

    /** 懒汉式 - 双重判断保证线程安全 */
    public static Channel getChannel() {
        if (Objects.nonNull(channel)) return channel;
        synchronized (LOCK) {
            if (Objects.nonNull(channel)) return channel;
            initChannel();
            return channel;
        }
    }


    public static void main(String[] args) {
        HelloService helloService = getProxyService(HelloService.class);
        System.out.println(helloService.sayHello("张三"));
//        System.out.println(helloService.sayHello("李四"));
//        System.out.println(helloService.sayHello("王五"));
    }

    /** 使用接口获取接口的代理类，并且此时代理类类型为接口类型, new Class[]{interfaceClass} 必须要这样写 */
    @SuppressWarnings({"unchecked"})
    public static <T> T getProxyService(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                int sequenceId = SequenceIdGenerator.nextId();
                // 1.构建RPC请求对象
                RpcRequestMessage message = new RpcRequestMessage(
                    sequenceId,
                    interfaceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    args
                );
                // 2.发送消息
                /** 同步 -- 只是将数据发出去 flush后，不会阻塞至服务端返回 */
//                getChannel().writeAndFlush(message).sync();
                getChannel().writeAndFlush(message);

                // 3.用promise来接收结果                              指定promise对象异步接收结果的线程
                DefaultPromise<Object> promise = new DefaultPromise<>(getChannel().eventLoop());
                RpcResponseMessageHandler.promiseMap.put(sequenceId, promise);
                // 4. 阻塞线程等待返回结果
                promise.await();
                if (promise.isSuccess()) {
                    return promise.getNow();
                } else {
                    throw new RuntimeException(promise.cause());
                }
            }
        });
    }

    /**
     * 初始化channel通道
     */
    private static void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();

        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        RpcResponseMessageHandler rpcResponseMessageHandler = new RpcResponseMessageHandler();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new ProtocolFrameDecoder());
                pipeline.addLast(loggingHandler);
                pipeline.addLast(messageCodec);
                pipeline.addLast(rpcResponseMessageHandler);
            }
        });
        try {
            channel = bootstrap.connect("127.0.0.1", 8080).sync().channel();
            /** 异步关闭 */
            channel.closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    group.shutdownGracefully();
                }
            });
        } catch (InterruptedException e) {
            log.error("client error", e);
        }
    }

}
