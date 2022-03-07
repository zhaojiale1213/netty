package com.itcast.nio.c2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/22 22:28
 * @Modified By:
 */
public class Client {

    public static void main(String[] args) throws IOException {
        // 获取通道
        SocketChannel sc = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
//        sc.connect(new InetSocketAddress("127.0.0.1", 9999));
        SocketAddress address = sc.getLocalAddress();
//        sc.write(Charset.defaultCharset().encode("hello\nworld\n"));
        sc.write(Charset.defaultCharset().encode("012345678\n9abcdefg3333\n"));
        // 切换非阻塞模式
//        sc.configureBlocking(false);
        System.in.read();
    }

}
