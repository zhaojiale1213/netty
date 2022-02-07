package com.zjl.cn.bio.demo2;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/1/19 23:08
 * @Modified By:
 */
public class Client {


    public static void main(String[] args) {

        // 循环发送
        try {
            Socket socket = new Socket("127.0.0.1", 9999);

            OutputStream outputStream = socket.getOutputStream();

            PrintStream printStream = new PrintStream(outputStream);

            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.print("请说：");
                String s = sc.nextLine();
                printStream.println(s);
                printStream.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
