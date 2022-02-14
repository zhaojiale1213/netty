package com.zjl.cn.nio.channel;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
