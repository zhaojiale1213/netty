package cn.itcast.server.handler;

import cn.itcast.message.GroupCreateRequestMessage;
import cn.itcast.message.GroupCreateResponseMessage;
import cn.itcast.server.session.Group;
import cn.itcast.server.session.GroupSession;
import cn.itcast.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Description: 创建组消息处理器
 * @Author: zjl
 * @Date:Created in 2022/3/31 21:44
 * @Modified By:
 */
@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);
        if (Objects.isNull(group)) {
            // 创建群成功消息
            ctx.writeAndFlush(new GroupCreateResponseMessage(true, groupName + "创建成功"));

            // 告诉群成员已被拉入群中消息
            List<Channel> channels = groupSession.getMembersChannel(groupName);
            for (Channel channel : channels) {
                // 排除自己
                if (channel.equals(ctx.channel())) continue;
                channel.writeAndFlush(
                    new GroupCreateResponseMessage(true, String.format("您已被拉入名叫%s的群中", groupName))
                );
            }
        } else {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false, groupName + "创建失败"));
        }
    }
}
