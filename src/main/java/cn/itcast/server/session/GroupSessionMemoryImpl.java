package cn.itcast.server.session;

import io.netty.channel.Channel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/24 21:47
 * @Modified By:
 */
public class GroupSessionMemoryImpl implements GroupSession {

    private final Map<String, Group> groupMap = new ConcurrentHashMap<>();


    @Override
    public Group createGroup(String name, Set<String> members) {
        // 不存在才put   Absent - 缺席的、不存在的
        return groupMap.putIfAbsent(name, new Group(name, members));
    }

    @Override
    public Group joinMember(String name, String member) {
        // 存在才操作
        return groupMap.computeIfPresent(name, new BiFunction<String, Group, Group>() {
            @Override
            public Group apply(String s, Group group) {
                group.getMembers().add(member);
                return group;
            }
        });
    }

    @Override
    public Group removeMember(String name, String member) {
        return groupMap.computeIfPresent(name, new BiFunction<String, Group, Group>() {
            @Override
            public Group apply(String s, Group group) {
                group.getMembers().remove(member);
                return group;
            }
        });
    }

    @Override
    public Group removeGroup(String name) {
        return groupMap.remove(name);
    }

    @Override
    public Set<String> getMembers(String name) {
        return groupMap.getOrDefault(name, Group.EMPTY_GROUP).getMembers();
    }

    @Override
    public List<Channel> getMembersChannel(String name) {
        return getMembers(name).stream().map(new Function<String, Channel>() {
            @Override
            public Channel apply(String s) {
                return SessionFactory.getSession().getChannel(s);
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
