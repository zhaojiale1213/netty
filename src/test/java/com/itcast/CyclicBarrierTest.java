package com.itcast;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * juc 循环栅栏 CyclicBarrier
 * 使用了一个缓存线程执行18个任务，任务要求每次处理的线程达到6个时候就释放一次，不足6个等待。
 */
public class CyclicBarrierTest {


    // 自定义工作线程
    private static class Worker extends Thread {
        private CyclicBarrier cyclicBarrier;

        public Worker(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "开始等待其他线程");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + "开始执行");
                // 工作线程开始处理，这里用Thread.sleep()来模拟业务处理
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "执行完毕");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int threadCount = 3;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount);

        for (int i = 0; i < threadCount; i++) {
            System.out.println("创建工作线程" + i);
            Worker worker = new Worker(cyclicBarrier);
            worker.start();
        }
    }


    @Test
    public void test() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        for (int i = 0; i < 6; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "开始等待其他线程");
                    try {
                        cyclicBarrier.await();
                        System.out.println(Thread.currentThread().getName() + "开始执行业务逻辑，耗时2秒");
                        TimeUnit.SECONDS.sleep(2L);
                        System.out.println(Thread.currentThread().getName() + "业务逻辑执行完毕");
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown();
    }



}
