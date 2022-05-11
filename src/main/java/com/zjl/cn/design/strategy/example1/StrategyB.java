package com.zjl.cn.design.strategy.example1;

import java.util.Arrays;

/**
 * 具体实现：
 * 策略B：去掉最高分和最低分后求平均分
 */
public class StrategyB implements AvStrategy{

    @Override
    public double getAverage(double[] a) {
        double score =0,sum = 0;
        Arrays.sort(a);
        for (int i = 1; i < a.length - 1; i++) {
            sum = sum + a[i];
        }
        score = sum/(a.length - 2);
        return score;
    }
}
