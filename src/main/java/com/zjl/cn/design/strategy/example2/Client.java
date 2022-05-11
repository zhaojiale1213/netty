package com.zjl.cn.design.strategy.example2;

public class Client {

    public static void main(String[] args) {
        PayContext payContext = new PayContext("小王", 5000d, new RMBPay());
        payContext.pay();

        PayContext context = new PayContext("tom", 3500d, new DollarPay());
        context.pay();

        PayContext ctx = new PayContext("小张", 40000, new AccountPay("1122"));
        ctx.pay();
    }

}
