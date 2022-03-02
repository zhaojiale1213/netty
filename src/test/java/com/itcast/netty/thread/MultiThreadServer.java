package com.itcast.netty.thread;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/28 22:39
 * @Modified By:
 */
@Slf4j
public class MultiThreadServer {

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        ssc.configureBlocking(false);

        Selector boss = Selector.open();
//        ssc.register(boss, SelectionKey.OP_ACCEPT);
        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);

        /** 在docker容器中拿到的是真实机器的核心数，而不是分配给docker的核心数，直到jdk10修复 */
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("Worker-" + i);
        }

        AtomicInteger count = new AtomicInteger();
        while (true) {
            boss.select();
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.info("=====>connected {} =====  before register {}", sc.getRemoteAddress(), sc.getRemoteAddress());
                    workers[count.getAndIncrement() % workers.length].register(sc);
                    log.info("after register {}", sc.getRemoteAddress());
                }
            }
        }
    }


     static class Worker implements Runnable {

        private Selector selector;
        private String name;
        private final AtomicBoolean start = new AtomicBoolean(false);
        /** CAS方式实现的线程安全的非阻塞式队列  - 无界 */
        private final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        public Worker(String name) {
            this.name = name;
        }

        public void register(SocketChannel sc) throws IOException {
            if (start.compareAndSet(false, true)) {
                selector = Selector.open();
                /** 设置线程要执行的任务 */
                Thread thread = new Thread(this, name);
                thread.start();
                log.info("啊，我执行了，start: {}", start.get());
            }
            /** 添加任务 */
            queue.offer(new Runnable() {
                @Override
                public void run() {
                    try {
                        /** register 必须在 selector 处于非阻塞状态下才可以*/
                        sc.register(selector, SelectionKey.OP_READ, null);
                        log.info("register =======>");
                    } catch (ClosedChannelException e) {
                        e.printStackTrace();
                    }
                }
            });
            selector.wakeup();  // 立即执行一次 selector.select()
            /** 或者直接调用 */
//            sc.register(selector, SelectionKey.OP_READ, null);

        }


        @Override
        public void run() {
            log.info("启动了：😄");
            while (true) {
                try {
                    selector.select();
                    /** 取出任务并执行, 直接调用run(), 当成普通方法执行，不会创建新线程*/
                    Runnable task = queue.poll();
                    if (Objects.nonNull(task)) {
                        task.run();
                        log.info("task: {}", task);
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel) key.channel();
                            log.info("read==========> {}", channel.getRemoteAddress());
                            channel.read(buffer);
                            buffer.flip();
                            System.out.println(Charset.defaultCharset().decode(buffer).toString());
                        } else if (key.isWritable()) {

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
