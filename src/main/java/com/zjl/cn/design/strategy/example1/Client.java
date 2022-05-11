package com.zjl.cn.design.strategy.example1;

public class Client {

    public static void main(String[] args) {
        //模拟评委打分
        double[] a = {1,6,8,4,5,7,4,7,8,9};

        //1.创建具体打分策略
        AvStrategy strategy = new StrategyA();

        //2.在创建策略上下文的同时，将具体的策略实现对象注入到策略上下文当中
        StrategyContext strategyContext = new StrategyContext(strategy);
        double average = strategyContext.getAverage(a);
        System.out.println("策略1：平均分：" + average );

        System.out.println("+++++++++++++++++++++++++");
        //使用第二种求平均分的策略：
        AvStrategy strategyB = new StrategyB();
        strategyContext.setStrategy(strategyB);

        //调用上下文对象的方法来完成对具体策略实现的回调
        average = strategyContext.getAverage(a);
        System.out.println("策略2：平均分：" + average );
    }

}
