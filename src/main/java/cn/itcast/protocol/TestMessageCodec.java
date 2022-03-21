package cn.itcast.protocol;

import cn.itcast.message.LoginRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/20 22:59
 * @Modified By:
 */
@Slf4j
public class TestMessageCodec {

    public static void main(String[] args) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
            new LoggingHandler(LogLevel.DEBUG),
            new MessageCodec()
        );

        // 测试出站 - encode
        LoginRequestMessage logMsg = new LoginRequestMessage("zhangsan", "123");
        channel.writeOutbound(logMsg);

        // 进站  -  decode
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, logMsg, buffer);

        channel.writeInbound(buffer);
    }

}
