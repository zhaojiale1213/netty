package com.itcast.nio.thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/1 22:03
 * @Modified By:
 */
public class TestClient {

    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
        sc.write(Charset.defaultCharset().encode("0123456789abcd"));
        System.in.read();
    }

}
