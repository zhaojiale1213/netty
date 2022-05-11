package com.zjl.cn.design.strategy.example2;

/**
 * 支付策略接口: 人民币支付、美金支付
 */
public interface PayStrategy {

    public void pay(PayContext ctx);
}
