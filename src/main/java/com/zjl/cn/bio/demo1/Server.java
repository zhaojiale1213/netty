package com.zjl.cn.bio.demo1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description: bio 同步阻塞式
 * @Author: zjl
 * @Date:Created in 2022/1/19 22:13
 * @Modified By:
 */
public class Server {

    public static void main(String[] args) {

        try {
            System.out.println("服务端启动！");
            // 服务端socket
            ServerSocket serverSocket = new ServerSocket(9999);

            // 阻塞 - 监听客户端的连接
            Socket socket = serverSocket.accept();
            System.out.println("客户端连接! ");

            InputStream inputStream = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            String msg;
            if ((msg = br.readLine()) != null) {
                System.out.println("服务端收到消息：" + msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
