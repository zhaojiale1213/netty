package com.zjl.cn.design.strategy.example1;

import java.util.Objects;

/**
 * 策略上下文
 */
public class StrategyContext {

    /**
     * 策略的引用
     */
    private AvStrategy strategy;

    public AvStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(AvStrategy strategy) {
        this.strategy = strategy;
    }

    public StrategyContext(AvStrategy strategy) {
        this.strategy = strategy;
    }

    public double getAverage(double[] a) {
        if (Objects.isNull(strategy)) return -1;
        return strategy.getAverage(a);
    }
}
