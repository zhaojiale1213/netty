package com.itcast.netty.c1;


import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/1/11 11:57
 * @Modified By:
 */
public class TestByteBufferExam {


    public static void main(String[] args) {
        /**
         * 数据间用 \n 分隔
         * 原始数据3条： Hello,world\n
         *              I'm zhangsan\n
         *              How are you?\n
         * 变成下面两个bytebuffer（粘包、半包）
         *      Hello,world\nI'm zhangsan\nHo
         *      w are you?\n
         *
         * 请恢复原样
         *
         */
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hi\nzh\nHo".getBytes(StandardCharsets.UTF_8));
        split(source);
        source.put("w-?\n".getBytes(StandardCharsets.UTF_8));
        split(source);

        System.out.println((int) '\n');
    }

    private static void split(ByteBuffer source) {
        //切换成读模式
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            // get(index) 不会修改 position
            if (source.get(i) == '\n') {
                // 每条字符串的长度  索引相减 + 1
                int length = i - source.position() + 1;
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                // 切换至读模式 - 打印输出
                target.flip();
                System.out.println(StandardCharsets.UTF_8.decode(target).toString());
            }
        }
        //切换至写模式
        source.compact();
    }
}
