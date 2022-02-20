package com.zjl.cn.nio.channel;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/11 21:42
 * @Modified By:
 */
@Slf4j
public class ChannelTest {


    /** 使用此方法copy */
    @Test
    public void test3() {
        try(
            FileChannel from = new FileInputStream("test.txt").getChannel();
            FileChannel to = new FileOutputStream("test03.txt").getChannel()
        ) {
            long size = from.size();

            // left 还剩余多少字节
            for (long left = size; left > 0;) {
                System.out.println("position: " + (size - left) + " left: " + left);
                // transferTo 返回实际传输的数据量
                left -= from.transferTo(size - left, left, to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test2() throws IOException {
        FileInputStream fis = new FileInputStream("test.txt");
        FileChannel from = fis.getChannel();

        FileOutputStream fos = new FileOutputStream("test02.txt");
        FileChannel to = fos.getChannel();

        // from  来源  输入端 -> 输出端
        //to.transferFrom(from, from.position(), from.size());

        // to  目标  输入端 -> 输出端
        /** 效率高，底层使用操作系统的零拷贝，默认限制2g大小数据 */
        from.transferTo(from.position(), from.size(), to);

        fis.close();
        fos.close();
        log.info("复制完成");
    }

    @Test
    public void test() {
        Map<String, String> map = System.getenv();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            log.info("{}: {}", entry.getKey(), entry.getValue());
        }
        log.info("--------------------------");
        log.info("--------------------------");
        Properties properties = System.getProperties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            log.info("{}: {}", entry.getKey().toString(), entry.getValue().toString());
        }
    }

    @Test
    public void write() throws IOException {
        FileOutputStream fos = new FileOutputStream("test.txt");
        FileChannel fileChannel = fos.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("hello, 我是赵佳乐".getBytes(StandardCharsets.UTF_8));
        buffer.flip();

        fileChannel.write(buffer);
        fileChannel.close();
        log.info("写入完成");
    }


    @Test
    public void read() throws IOException {
        FileInputStream fis = new FileInputStream("test.txt");
        FileChannel channel = fis.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        channel.read(buffer);
        buffer.flip();

        log.info(StandardCharsets.UTF_8.decode(buffer).toString());
    }


    @Test
    public void copy() throws IOException {
        FileInputStream fis = new FileInputStream("D:\\BaiduNetdiskDownload\\soft\\typora-setup-x64.exe");
        FileOutputStream fos = new FileOutputStream("D:\\BaiduNetdiskDownload\\soft\\typora-copy3.exe");

        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);

        while (true) {
            buffer.clear();

            int read = fisChannel.read(buffer);
            if (read == -1) break;

            buffer.flip();
            fosChannel.write(buffer);
        }

        fisChannel.close();
        fosChannel.close();
        fis.close();
        fos.close();

        log.info("copy完成");
    }

}
