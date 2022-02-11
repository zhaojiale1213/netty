package com.zjl.cn.proxy;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/11 16:24
 * @Modified By:
 */
public interface Subject {

    /**
     * 你好
     *
     * @param name
     * @return
     */
    public String SayHello(String name);

    /**
     * 再见
     *
     * @return
     */
    public String SayGoodBye();

}
