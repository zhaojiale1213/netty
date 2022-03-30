package cn.itcast.server.handler;

import cn.itcast.message.ChatRequestMessage;
import cn.itcast.message.ChatResponseMessage;
import cn.itcast.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Objects;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/30 22:58
 * @Modified By:
 */
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String from = msg.getFrom();
        String to = msg.getTo();
        Channel channel = SessionFactory.getSession().getChannel(to);
        if (Objects.nonNull(channel)) {
            channel.writeAndFlush(new ChatResponseMessage(from, msg.getContent()));
        } else {
            ctx.writeAndFlush(new ChatResponseMessage(false, "对方不在线或者不存在"));
        }
    }
}
