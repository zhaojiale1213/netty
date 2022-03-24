package cn.itcast.server.service;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/23 22:36
 * @Modified By:
 */
public abstract class UserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}
