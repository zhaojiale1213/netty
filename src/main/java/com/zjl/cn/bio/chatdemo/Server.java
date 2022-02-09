package com.zjl.cn.bio.chatdemo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description: bio模式下的端口转发思想 - 服务端实现（客户端群聊）
 * @Author: zjl
 * @Date:Created in 2022/2/9 21:38
 * @Modified By:
 */
public class Server {

    private static final List<Socket> sockets = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            while (true) {
                Socket socket = serverSocket.accept();
                sockets.add(socket);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 接收客户端的消息
                            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            String msg;
                            while ((msg = br.readLine()) != null) {
                                // 发送给其它客户端
                                for (Socket socket1 : sockets) {
                                    if (Objects.equals(socket, socket1)) continue;
                                    PrintStream ps = new PrintStream(socket1.getOutputStream());
                                    ps.println(msg);
                                    ps.flush();
                                }
                            }
                        } catch (IOException e) {
                            System.out.println("socket 关闭");
                            sockets.remove(socket);
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
