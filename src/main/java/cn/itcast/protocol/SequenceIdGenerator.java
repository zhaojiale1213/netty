package cn.itcast.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/4/17 22:46
 * @Modified By:
 */
public abstract class SequenceIdGenerator {

    private static final AtomicInteger id = new AtomicInteger();

    public static int nextId() {
        return id.incrementAndGet();
    }

}
