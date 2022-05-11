package com.zjl.cn.design.strategy.example2;

//支付到银行账户的策略
public class AccountPay implements PayStrategy{

    //银行账户
    private String account;

    public AccountPay(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public void pay(PayContext ctx) {
        System.out.println("现在给："+ctx.getUsername()+"的账户："+getAccount()+" 支付工资："+ctx.getMoney()+" 元！");
    }

}
