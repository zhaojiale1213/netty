package cn.itcast.server.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/4/3 9:44
 * @Modified By:
 */
@Slf4j
public class IdleServerHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE == idleStateEvent.state()) {
                log.info("读空闲已超过 5s");
                ctx.channel().close();
            }
        }
    }
}
