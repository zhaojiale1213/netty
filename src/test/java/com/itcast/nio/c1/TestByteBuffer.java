package com.itcast.nio.c1;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/1/3 11:13
 * @Modified By:
 */
@Slf4j
public class TestByteBuffer {


    public static void main(String[] args) {
        byte[] bytes = {'a', 'b', 'c'};
        System.out.println(Arrays.toString(bytes));

        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                int read = channel.read(buffer);
                log.info("读到的字节数 {}", read);
                if (read == -1) break;
                //切换至读模式
                buffer.flip();
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    log.info("实际字节 {}", (char)b);
                }
                //切换为写模式
//                buffer.clear();
                buffer.compact();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test() {

        /**
         * java.nio.HeapByteBuffer  java 堆内存，读写效率低，会受GC影响
         */
        ByteBuffer allocate = ByteBuffer.allocate(16);
        System.out.println(allocate.getClass());
        System.out.println(allocate.getClass().getName());

        /**
         * java.nio.DirectByteBuffer 系统内存（直接内存），读写效率高（少一次拷贝），不受GC影响， 创建（分配）内存效率低
         *                           还有可能造成内存泄漏（都有这个可能）
         */
        ByteBuffer direct = ByteBuffer.allocateDirect(16);
        System.out.println(direct.getClass());
        System.out.println(direct.getClass().getName());
    }


    /**
     * 字符串与 ByteBuffer 相互转化
     */
    @Test
    public void test2() {
        String s = "hello";

        ByteBuffer buffer1 = ByteBuffer.allocate(16);
        buffer1.put(s.getBytes(StandardCharsets.UTF_8));
        // 此时还处于写模式
        buffer1.flip();
        String s1 = StandardCharsets.UTF_8.decode(buffer1).toString();
        System.out.println(s1);


        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode(s);
        System.out.println(StandardCharsets.UTF_8.decode(buffer2).toString());
    }


    @Test
    public void test3() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("a", 2);
        System.out.println(map);
    }

}
