package com.zjl.cn.proxy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/11 16:29
 * @Modified By:
 */
public class Test {

    public static void main(String[] args) throws IOException {
        String s = "ABC";
        System.out.println(testStr(s));
        System.out.println(s);

        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println("path: " + path);
        File file = new File(path);
        System.out.println("file: " + file.getName());
        System.out.println("file: " + file.getPath());
        System.out.println("file: " + file.getCanonicalPath());
        System.out.println("file: " + file.getAbsolutePath());

    }


    private static String testStr(String str) {
        str = "abc";
        return str;
    }

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

        System.out.println(Runtime.getRuntime().availableProcessors());
    }


    @org.junit.Test
    public void test3() {
        ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<Integer>(5);
        arrayBlockingQueue.offer(1);
        System.out.println(arrayBlockingQueue.poll());
        System.out.println(arrayBlockingQueue.poll());

        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();

        linkedBlockingQueue.offer("a");
        System.out.println(linkedBlockingQueue.poll());
        System.out.println(linkedBlockingQueue.poll());
    }


    @org.junit.Test
    public void test4() {
        AtomicInteger count = new AtomicInteger(50);

        for (int i = 0; i < 50; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    count.decrementAndGet();
                    System.out.println("线程" + Thread.currentThread().getName() + "购票成功，剩余：" + count);

                }
            }).start();
        }
    }

    @org.junit.Test
    public void test5() {
        String path = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(path);
        String path1 = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(path1);

        File file = new File(path);
        String name = file.getName();
        System.out.println(name);

        Class<? extends Test> clazz = this.getClass();
        System.out.println(clazz.getResource("").getPath());
        System.out.println(clazz.getName());
        System.out.println(clazz.getSimpleName());

    }


    @org.junit.Test
    public void test6() {
        String s = "Abc";
        System.out.println((int)'A');
        System.out.println((int)'Z');
        System.out.println((int)'a');
        System.out.println((int)'z');

        System.out.println((int)'1');
        System.out.println('1' - 1);
    }


}
