package com.zjl.cn.spi;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/4/21 22:20
 * @Modified By:
 */
public class America implements Say{

    @Override
    public String sayHello(String name) {
        return name + " hello!";
    }

}
