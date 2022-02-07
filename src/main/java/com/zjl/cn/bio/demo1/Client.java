package com.zjl.cn.bio.demo1;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @Description: bio 同步阻塞式
 * @Author: zjl
 * @Date:Created in 2022/1/19 22:13
 * @Modified By:
 */
public class Client {

    public static void main(String[] args) {

        // 发送一次
        try {
            Socket socket = new Socket("127.0.0.1", 9999);

            OutputStream outputStream = socket.getOutputStream();

            PrintStream printStream = new PrintStream(outputStream);
            printStream.println("hello world!");
            printStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
