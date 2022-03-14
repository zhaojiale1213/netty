package com.itcast.netty.byteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import org.junit.Test;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/13 20:56
 * @Modified By:
 */
public class TestByteBuf {

    @Test
    public void test() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        System.out.println(buf);

        // 逻辑切片过程中，使用同一物理内存，无数据复制, 切片后新的 ByteBuf 无法写入 - 固定长度
        /** 切片后的新 ByteBuf 自己负责释放内存 - release ,  retain - 避免原 ByteBuf 释放后影响使用*/
        ByteBuf b1 = buf.slice(0, 5); // 前五个
        b1.retain();
        ByteBuf b2 = buf.slice(5, 5); // 后五个
        b2.retain();

        System.out.println(b1);
        System.out.println(b2);

        b1.release();
        b2.release();
    }


    /**
     * compositeBuffer  将多个小的 ByteBuf 逻辑组成一个，减小拷贝
     */
    @Test
    public void test1() {
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[] {1, 2, 3, 4, 5});
        buf1.retain();

        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        buf2.writeBytes(new byte[] {6, 7, 8, 9, 10});
        buf2.retain();

        CompositeByteBuf byteBufs = ByteBufAllocator.DEFAULT.compositeBuffer();
        byteBufs.addComponents(true, buf1, buf2);
        System.out.println(byteBufs);

        buf1.release();
        buf2.release();
    }

}
