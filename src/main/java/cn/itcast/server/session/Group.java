package cn.itcast.server.session;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Collections2;
import lombok.Data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/23 22:56
 * @Modified By:
 */
@Data
public class Group {

    // 聊天室名称
    private String name;
    // 聊天室成员
    private Set<String> members;

    public static final Group EMPTY_GROUP = new Group("empty", Collections.emptySet());

    public Group(String name, Set<String> members) {
        this.name = name;
        this.members = CollectionUtil.isEmpty(members) ? new HashSet<>() : members;
    }

}
