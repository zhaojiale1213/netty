package cn.itcast.server.session;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/23 22:53
 * @Modified By:
 */
public abstract class SessionFactory {

    private static final Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }

}
