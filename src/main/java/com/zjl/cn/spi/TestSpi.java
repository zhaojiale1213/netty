package com.zjl.cn.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/4/21 22:22
 * @Modified By:
 */
public class TestSpi {

    /**
     * 定义服务的通用接口，针对通用的服务接口，提供具体的实现类。
     *
     * 在jar包（服务提供者）的META-INF/services/目录中，新建一个文件，文件名为SPI接口的"全限定名"。
     * 文件内容为该接口的具体实现类的"全限定名"
     *
     * 将spi所在jar放在主程序的classpath中
     *
     * 服务调用方使用java.util.ServiceLoader去动态加载具体的实现类到JVM中
     */

    public static void main(String[] args) {
        ServiceLoader<Say> sayServices = ServiceLoader.load(Say.class);
        Iterator<Say> iterator = sayServices.iterator();
        while (iterator.hasNext()) {
            Say say = iterator.next();
            System.out.println(say.getClass().getName());
            System.out.println(say.sayHello("张三"));
        }
    }

}
