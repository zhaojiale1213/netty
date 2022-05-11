package com.zjl.cn.design.strategy.example3;

/**
 * 申通快递邮费计算，实现策略接口：
 */
public class CalculateStrategySto implements CalculateStrategy {
    @Override
    public Double calculate(Integer weight) {
        return 12 + weight * 0.8;
    }
}
