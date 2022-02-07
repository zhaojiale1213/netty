package com.zjl.cn.bio.demo2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/1/19 23:08
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
            while ((msg = br.readLine()) != null) {
                if (msg.equals("close")) break;
                System.out.println("服务端收到消息：" + msg);
            }
            System.out.println("再见了！！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
