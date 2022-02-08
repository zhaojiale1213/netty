package com.zjl.cn.bio.file;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * @Description: 接收文件
 * @Author: zjl
 * @Date:Created in 2022/2/8 14:21
 * @Modified By:
 */
public class Server {


    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true) {
                Socket socket = serverSocket.accept();

                download(socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void download(Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                try {
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    String fileType = dis.readUTF();
                    System.out.println("文件类型：" + fileType);

                    os = new FileOutputStream("D:\\B\\" + UUID.randomUUID().toString() + fileType);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = dis.read(buffer)) > 0) {
                        os.write(buffer, 0, len);
                    }
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
