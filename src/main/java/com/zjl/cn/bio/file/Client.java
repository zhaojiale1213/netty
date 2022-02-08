package com.zjl.cn.bio.file;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;

/**
 * @Description: 文件上传
 * @Author: zjl
 * @Date:Created in 2022/2/8 14:20
 * @Modified By:
 */
public class Client {

    public static void main(String[] args) {
        upload("D:\\A\\bc84ed12becafd576220eec50e54184.jpg");
    }


    public static void upload(String filePath) {
        try(InputStream inputStream = new FileInputStream(filePath)) {
            Socket socket = new Socket("127.0.0.1", 8888);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String fileType = filePath.substring(filePath.lastIndexOf("."));
            System.out.println("文件类型：" + fileType);

            dos.writeUTF(fileType);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                dos.write(buffer, 0, len);
            }
            dos.flush();
            /** 通知服务端数据发送完毕 */
            socket.shutdownOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
