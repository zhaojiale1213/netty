package cn.itcast.server.handler;

import cn.hutool.json.JSONUtil;
import cn.itcast.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 客户端退出处理器
 * @Author: zjl
 * @Date:Created in 2022/4/1 21:24
 * @Modified By:
 */
@Slf4j
@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {

    /** 连接断开时触发 */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SessionFactory.getSession().unbind(channel);
        log.info("{} - 断开连接 - channelInactive", JSONUtil.toJsonStr(channel));
    }


    /** 异常断开时会调用这个方法和上面这个方法 */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        SessionFactory.getSession().unbind(channel);
        log.info("{} - 异常断开 - {}", JSONUtil.toJsonStr(channel), cause.getMessage());
    }
}
