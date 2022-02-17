package com.zjl.cn.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Objects;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/17 20:51
 * @Modified By:
 */
public class Server {

    private Selector selector;

    private ServerSocketChannel ssChannel;

    private static final int PORT = 9999;

    public Server() {
        try {
            // 创建选择器对象
            this.selector = Selector.open();
            // 获取通道
            this.ssChannel = ServerSocketChannel.open();
            // 绑定端口
            ssChannel.bind(new InetSocketAddress(PORT));
            // 设置非阻塞模式
            ssChannel.configureBlocking(false);
            // 绑定选择器，指定监听事件为 接收连接事件
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Server server = new Server();

        server.listen();
    }

    private void listen() {
        try {
            while (selector.select() > 0) {
                // selectedKeys  有事件发生的key, 用完要remove
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = ssChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        readClientData(key);
                    }

                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readClientData(SelectionKey key) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            if (read > 0) {
                buffer.flip();
                String msg = new String(buffer.array(), 0, buffer.remaining());
                System.out.println("接收客户端消息：" + msg);

                //推送消息给其他客户端
                sendMsgToClient(msg, channel);
            }
        } catch (Exception e) {
            try {
                key.cancel();
                if (Objects.nonNull(channel)) {
                    System.out.println("客户端离线：" + channel.getRemoteAddress());
                    channel.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


    private void sendMsgToClient(String msg, SocketChannel channel) throws IOException {
        System.out.println("转发消息，处理线程：" + Thread.currentThread().getName());
        // keys 所有注册的key
        for (SelectionKey key : selector.keys()) {
            // 排除服务端 ServerSocketChannel
            if (key.channel() instanceof ServerSocketChannel) continue;
            SocketChannel c = (SocketChannel)key.channel();
            if (Objects.equals(c, channel)) continue;
            ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
            c.write(buffer);
        }
    }


}
