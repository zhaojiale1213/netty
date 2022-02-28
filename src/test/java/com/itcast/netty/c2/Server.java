package com.itcast.netty.c2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/22 22:28
 * @Modified By:
 */
@Slf4j
public class Server {

    /**
     * SelectionKey.OP_ACCEPT ：服务端接收建立连接请求时触发
     * SelectionKey.OP_CONNECT ：客户端接待服务端建立连接后触发
     * SelectionKey.OP_READ ： 服务端接收数据（可读事件）
     * SelectionKey.OP_WRITE ： 服务端可写事件（数据量大，一次无法写完）
     */

    public static void main(String[] args) throws IOException {
        // 获取通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 切换为非阻塞模式
        ssc.configureBlocking(false);
        // 绑定端口
        ssc.bind(new InetSocketAddress(9999));
        // 获取选择器
        Selector selector = Selector.open();
        // 0 - 不关注任何事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // 设置只关注 accept 事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.info("register key: {}", sscKey);

        while (true) {
            // select() 没事件发生，阻塞，有事件，继续运行
            selector.select();
            // selectedKeys() 包含所有发生的事件, 不会主动删除事件
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if (key.isAcceptable()) {
                    // 取消事件 - 也是处理掉了
//                key.cancel();
                    log.info("Accept key: {}", key);
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);

                    /** 设置附件属性 */
                    ByteBuffer buffer = ByteBuffer.allocate(10);
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    /** 传输数据超过缓冲区大小时，会多次触发读事件，直到传输完毕 */
                    log.info("Read key: {}", key);
                    /** 客户端关异常闭连接 read 会报 IOException, 关闭时会发送一个read事件 */
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        // 获取附件
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        /** 客户端主动断开， read = -1 关流， read = 0, 读到的数据为0 */
                        int read = channel.read(buffer);
                        if (read == -1) {
                            key.cancel();
                        } else {
                            split(buffer);
                            /** 消息大小超过ByteBuffer,未读到分隔符, 扩容 */
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(2 * buffer.capacity());
                                buffer.flip();
                                newBuffer.put(buffer);
                                // 重新绑定附件
                                key.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.info("key: " + key + " 下线了");
                        /** 客户端关闭后，从 selectedKeys集合 中移除这个key */
                        key.cancel();
                    }
                }


                iter.remove();
            }
        }
    }


    private static void split(ByteBuffer source) {
        //切换成读模式
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            // get(index) 不会修改 position
            if (source.get(i) == '\n') {
                // 每条字符串的长度  索引相减 + 1
                int length = i - source.position() + 1;
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                // 切换至读模式 - 打印输出
                target.flip();
                System.out.println("服务端接收：" + Charset.defaultCharset().decode(target).toString());
            }
        }
        //切换至写模式
        source.compact();
    }

}
