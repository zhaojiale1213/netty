package com.itcast;

import java.util.concurrent.Semaphore;

/**
 * juc 信号量 Semaphore
 */
public class SemaphoreTest {

    public static void main(String[] args) {

        //定义semaphore实例，设置许可数为3，即停车位为3个
        Semaphore semaphore = new Semaphore(3);

        //创建五个线程，即有5辆汽车准备进入停车场停车
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "尝试进入停车场...");
                    //尝试获取许可，未获取到阻塞
                    semaphore.acquire();
                    //模拟停车
                    long time = (long) (Math.random() * 10 + 1);
                    System.out.println(Thread.currentThread().getName() + "进入了停车场，停车" + time +
                            "秒...");
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(Thread.currentThread().getName() + "开始驶离停车场...");
                    //释放许可
                    semaphore.release();
                    System.out.println(Thread.currentThread().getName() + "离开了停车场！");
                }
            }, i + "号汽车").start();
        }
    }
}
