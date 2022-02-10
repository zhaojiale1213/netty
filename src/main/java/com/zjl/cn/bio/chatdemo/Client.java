package com.zjl.cn.bio.chatdemo;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/9 22:15
 * @Modified By:
 */
public class Client {

    public static void main(String[] args) {

        // 循环发送
        try {
            Socket socket = new Socket("127.0.0.1", 9999);

            // 发消息
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PrintStream printStream = null;
                    try {
                        printStream = new PrintStream(socket.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scanner sc = new Scanner(System.in);
                    while (true) {
                        System.out.println("请说：");
                        String s = sc.nextLine();
                        printStream.println(s);
                        printStream.flush();
                    }
                }
            }).start();

            //收消息
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String msg;
                        while ((msg = br.readLine()) != null) {
                            System.out.println("收到：" + msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
