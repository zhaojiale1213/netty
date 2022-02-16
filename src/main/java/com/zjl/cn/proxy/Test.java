package com.zjl.cn.proxy;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

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

    @org.junit.Test
    public void test2() {
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 8888);
        System.out.println(socketAddress.getHostName());
        System.out.println(socketAddress.getHostString());
        System.out.println(socketAddress.getPort());
    }

}
