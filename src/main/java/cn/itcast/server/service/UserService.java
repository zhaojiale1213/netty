package cn.itcast.server.service;

/**
 * @Description: 用户管理接口
 * @Author: zjl
 * @Date:Created in 2022/3/23 22:35
 * @Modified By:
 */
public interface UserService {

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回 true, 否则返回 false
     */
    boolean login(String username, String password);

}
