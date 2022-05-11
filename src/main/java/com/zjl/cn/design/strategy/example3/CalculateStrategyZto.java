package com.zjl.cn.design.strategy.example3;

/**
 * 中通快递邮费计算，实现策略接口：
 */
public class CalculateStrategyZto implements CalculateStrategy {
    @Override
    public Double calculate(Integer weight) {
        return 9 + weight * 1.1;
    }
}
