package com.zjl.cn.nio.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/16 21:53
 * @Modified By:
 */
public class Client {

    public static void main(String[] args) throws IOException {
        // 获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        // 切换非阻塞模式
        socketChannel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("请说：");
            String msg = scanner.nextLine();
            buffer.put(msg.getBytes(StandardCharsets.UTF_8));
            buffer.flip();

            socketChannel.write(buffer);
            buffer.clear();
        }
    }
}
