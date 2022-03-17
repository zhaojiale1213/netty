package com.itcast.advance;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.StandardCharsets;

/**
 * @Description: LTC 解码器  L - length, T - type, C - content
 *               0x 代表 16进制  1个字节对应2个16进制字符
 *               0x000c -->  000c  -->  2个字节  -->  对应10进制：12
 * @Author: zjl
 * @Date:Created in 2022/3/16 22:50
 * @Modified By:
 */
public class TestLengthFieldDecoder {

    /** 0x 代表 16进制  1个字节对应2个16进制字符 */
    /** 0x000c -->  000c  -->  2个字节  -->  对应10进制：12 */
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(

            /** maxFrameLength - 最大长度(超过会报错)， lengthFieldOffset - 长度字段偏移（偏移量之后才是长度）
             * lengthFieldLength - 长度占用字节数   lengthAdjustment - 长度字段之后，跳过几个字节后才是content
             * initialBytesToStrip - 从头剥离几个字节（比如 剥离length, 长度占用字节为4，则为4） -- 舍弃前面几个字节
             * */
            new LengthFieldBasedFrameDecoder(1024, 0, 4, 1, 4),
            new LoggingHandler(LogLevel.DEBUG)
        );

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

        //4个字节的内容长度， 实际内容   length + 1 + content
        send(buffer, "hello, world");
        send(buffer, "呵呵");

        channel.writeInbound(buffer);
    }

    private static void send(ByteBuf buf, String content) {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        int length = bytes.length;
        buf.writeInt(length);
        buf.writeByte(1);
        buf.writeBytes(bytes);
    }

}
