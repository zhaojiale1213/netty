package com.itcast.nio.c2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/28 20:18
 * @Modified By:
 */
public class WriteClient {

    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));

        // 接收数据
        int count = 0;
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1000);
            count += sc.read(buffer);
            System.out.println("接收：" + count);
            buffer.clear();
        }

    }
}
