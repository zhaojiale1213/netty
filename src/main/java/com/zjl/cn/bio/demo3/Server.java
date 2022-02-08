package com.zjl.cn.bio.demo3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/3 10:55
 * @Modified By:
 */
public class Server {


    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);

            // 定义一个循环，不断接收客户端的socket的连接
            while (true) {
                Socket socket = serverSocket.accept();
                new ServerThreadReader(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
