package cn.itcast.server.service;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/4/16 17:49
 * @Modified By:
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        int i = 1/0;
        return "你好, " + name;
    }

}
