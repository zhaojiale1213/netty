package com.zjl.cn.design.proxy;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/11 16:25
 * @Modified By:
 */
public class RealSubject implements Subject {

    /**
     * 你好
     *
     * @param name
     * @return
     */
    @Override
    public String SayHello(String name) {
        return "hello " + name;
    }

    /**
     * 再见
     *
     * @return
     */
    @Override
    public String SayGoodBye() {
        return " good bye ";
    }


}
