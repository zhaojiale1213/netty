package com.zjl.cn.callback;

import cn.hutool.json.JSONUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TestCompletableFuture {

    @Test
    public void test1() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 在执行");
                try {
                    TimeUnit.SECONDS.sleep(3L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, executorService);
        System.out.println(Thread.currentThread().getName() + " 等待、、、、");
        while (true) {
            if (future.isDone()) {
                System.out.println(Thread.currentThread().getName() + " CompletedFuture...isDown");
                break;
            }
        }
    }


    @Test
    public void test2() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(new Supplier<Object>() {
            @Override
            public Object get() {
                try {
                    TimeUnit.SECONDS.sleep(3L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("supplyAsync " + Thread.currentThread().getName());
                return "hello ";
            }
        }, executorService).thenAccept(new Consumer<Object>() {
            @Override
            public void accept(Object o) {
                System.out.println(o + " world");
            }
        });
        System.out.println(Thread.currentThread().getName() + " 等待、、、、");
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test3() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("CompletableFuture 监管");
                future.complete("任务返回结果");
            }
        }).start();
        System.out.println(future.get());
    }

    @Test
    public void test4() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.forName("com.zjl.cn.callback.Student");
        Object newInstance = clazz.getDeclaredConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(newInstance, "张三");
        }
        System.out.println(newInstance);

    }

}
