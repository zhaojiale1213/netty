package com.zjl.cn.proxy;

import java.lang.reflect.Proxy;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/11 16:29
 * @Modified By:
 */
public class Test {


    @org.junit.Test
    public void test() {

        Subject subject = new RealSubject();

        Subject proxyInstance = (Subject) Proxy.newProxyInstance(subject.getClass().getClassLoader(), subject.getClass().getInterfaces(),
                new InvocationHandlerImpl(subject));

        System.out.println(proxyInstance.SayHello("???"));

    }

    @org.junit.Test
    public void test1() {

        Subject proxy = (Subject) ProxyFactory.proxy(new RealSubject());

        System.out.println(proxy.SayHello("代理"));

    }

}
