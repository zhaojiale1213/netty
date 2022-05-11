package com.zjl.cn.design.strategy.example3;

/**
 * 京东快递邮费计算，实现策略接口：
 */
public class CalculateStrategyJd implements CalculateStrategy {
    @Override
    public Double calculate(Integer weight) {
        return 10 + weight * 1.2;
    }
}
