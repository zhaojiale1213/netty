package com.zjl.cn.design.strategy.example1;

/**
 * 具体实现：
 * 策略A：直接求平均分
 */
public class StrategyA implements AvStrategy{

    @Override
    public double getAverage(double[] a) {
        double score =0,sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum = sum + a[i];
        }
        score = sum/a.length;
        return score;
    }
}
