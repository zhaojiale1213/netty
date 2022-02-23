package com.itcast.netty.c2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/22 22:28
 * @Modified By:
 */
public class Client {

    public static void main(String[] args) throws IOException {
        // 获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        // 切换非阻塞模式
        socketChannel.configureBlocking(false);
        System.out.println("wait****************");
    }

}
