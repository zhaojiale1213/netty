package cn.itcast.server.session;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/31 21:54
 * @Modified By:
 */
public abstract class GroupSessionFactory {

    private static final GroupSession groupSession = new GroupSessionMemoryImpl();

    public static GroupSession getGroupSession() {
        return groupSession;
    }

}
