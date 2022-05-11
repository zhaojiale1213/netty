package com.zjl.cn.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/11 16:36
 * @Modified By:
 */
public class ProxyFactory {


    /**
     *
     * @param t - 被代理对象
     * @return
     */
    public static Object proxy(Object t) {

        return Proxy.newProxyInstance(t.getClass().getClassLoader(), t.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("前置处理");

                System.out.println("Method: " + method);
                Object val = method.invoke(t, args);

                System.out.println("后置处理");
                return val;
            }
        });
    }

}
