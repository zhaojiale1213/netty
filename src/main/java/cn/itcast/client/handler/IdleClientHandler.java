package cn.itcast.client.handler;

import cn.itcast.message.PingMessage;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 客户端发送心跳包，避免服务端断开连接
 * @Author: zjl
 * @Date:Created in 2022/4/4 12:08
 * @Modified By:
 */
@Slf4j
public class IdleClientHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (IdleState.WRITER_IDLE == idleStateEvent.state()) {
                log.info("客户端发送心跳包");
                ctx.writeAndFlush(new PingMessage());
            }
        }
    }
}
