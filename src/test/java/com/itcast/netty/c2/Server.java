package com.itcast.netty.c2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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
     * SelectionKey.OP_WRITE ： 可写事件
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
            // selectedKeys() 包含所有发生的事件
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                log.info("key: {}", key);
                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                channel.accept();
            }
        }
    }

}
