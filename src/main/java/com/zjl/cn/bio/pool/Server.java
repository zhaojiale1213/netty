package com.zjl.cn.bio.pool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/8 13:23
 * @Modified By:
 */
public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            while (true) {
                Socket socket = serverSocket.accept();
                HandlerSocketServerPool pool = new HandlerSocketServerPool();
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputStream inputStream = socket.getInputStream();
                            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
                            String msg;
                            while ((msg = bf.readLine()) != null) {
                                /** 释放被占用的线程, 不跳出循环会被一直占用 */
                                if (msg.equals("close")) break;
                                System.out.println("服务端收到：" + msg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
