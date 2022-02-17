package com.zjl.cn.nio.demo1;

import lombok.extern.slf4j.Slf4j;

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
 * @Description: NIO非阻塞通信模式下入门案例
 * @Author: zjl
 * @Date:Created in 2022/2/16 17:01
 * @Modified By:
 */
@Slf4j
public class Server {


    public static void main(String[] args) throws IOException {
        System.out.println("服务端启动----------------");

        // 获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        // 切换为非阻塞模式
        ssChannel.configureBlocking(false);
        // 绑定端口
        ssChannel.bind(new InetSocketAddress(9999));
        // 获取选择器
        Selector selector = Selector.open();
        // 绑定选择器，指定监听事件为 接收连接事件
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 阻塞式方法，等待客户端连接
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 接收事件
                if (selectionKey.isAcceptable()) {
                    // 获取当前接入的客户端通道
                    SocketChannel socketChannel = ssChannel.accept();
                    socketChannel.configureBlocking(false);
                    // 绑定选择器，指定监听事件为 读事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int len;
                    while ( (len = socketChannel.read(byteBuffer)) > 0) {
                        byteBuffer.flip();
                        log.info("服务端接收：{}", new String(byteBuffer.array(), 0, len));
                        byteBuffer.clear();
                    }
                }

                // 处理完后，移除当前事件
                iterator.remove();
            }

        }

    }

}
