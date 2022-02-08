package com.zjl.cn.bio.pool;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/8 14:05
 * @Modified By:
 */
public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9999);
            PrintStream printStream = new PrintStream(socket.getOutputStream());

            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.print("请说：");
                String s = sc.nextLine();
                printStream.println(s);
                printStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
