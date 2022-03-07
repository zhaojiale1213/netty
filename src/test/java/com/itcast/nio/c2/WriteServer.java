package com.itcast.nio.c2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/28 19:51
 * @Modified By:
 */
public class WriteServer {


    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(9999));

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0) {
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);

                    SelectionKey sKey = sc.register(selector, SelectionKey.OP_READ);
                    // 写入大量数据，无法一次性写入，会阻塞在 write()方法
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 30000000; i++) {
                        sb.append("a");
                    }

                    ByteBuffer buffer = ByteBuffer.wrap(sb.toString().getBytes(StandardCharsets.UTF_8));

                    // 返回值代表实际写入的字节
                    int write = sc.write(buffer);
                    System.out.println("写入字节：" + write);

                    if (buffer.hasRemaining()) {
                        /** 同时注册读写事件  sKey.interestOps(SelectionKey.OP_WRITE) 会覆盖掉原有事件*/
                        sKey.interestOps(sKey.interestOps() + SelectionKey.OP_WRITE);
                        // 将数据存下来
                        sKey.attach(buffer);
                    }
                } else if (key.isWritable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    int write = sc.write(buffer);
                    System.out.println("实际写入字节：" + write);
                    // 写完清理数据，buffer 很大
                    if (!buffer.hasRemaining()) {
                        /** 清理数据和事件 */
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                        key.attach(null);
                        System.out.println("清理");
                    }
                }
            }
        }
    }

}
