package com.zjl.cn.design.strategy.example2;

//人民币支付策略
public class RMBPay implements PayStrategy {
    @Override
    public void pay(PayContext ctx) {
        System.out.println("现在给："+ctx.getUsername()+" 人民币支付 "+ctx.getMoney()+"元！");
    }
}

