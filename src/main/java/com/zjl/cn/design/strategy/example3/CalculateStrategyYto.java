package com.zjl.cn.design.strategy.example3;

/**
 * 圆通快递邮费计算，实现策略接口：
 */
public class CalculateStrategyYto implements CalculateStrategy {
    @Override
    public Double calculate(Integer weight) {
        return 8 + weight * 1.5;
    }
}
