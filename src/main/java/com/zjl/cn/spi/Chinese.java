package com.zjl.cn.spi;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/4/21 22:16
 * @Modified By:
 */
public class Chinese implements Say {

    @Override
    public String sayHello(String name) {
        return name + " 你好！";
    }
}
