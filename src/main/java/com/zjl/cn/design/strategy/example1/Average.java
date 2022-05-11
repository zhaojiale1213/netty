package com.zjl.cn.design.strategy.example1;

import java.util.Arrays;

/**
 * 未使用策略模式, 需要 很多if else
 * 假如：有一场演讲比赛，有十个评委对参赛选手的成绩进行打分，但最终要通过评委的平均分来决定选手的名次。现在有两种求平均分的策略：
 * 第一种：将十名裁判的分加起来求平均值。
 * 第二种：去掉最高分和最低分后求平均值。
 */
public class Average {

    /**
     * @param a        每个裁判所打的分数
     * @param strategy 使用的策略
     * @return
     */
    public double getScore(double[] a, String strategy) {

        double score = 0d;
        double sum = 0d;

        //使用第一种策略打分
        if ("策略一".equals(strategy)) {
            System.out.println("使用策略1计算分数！");
            for (int i = 0; i < a.length; i++) {
                sum = sum + a[i];
            }
            score = sum / a.length;

            //使用第二种策略打分
        } else {
            System.out.println("使用策略2计算分数！");
            Arrays.sort(a);
            for (int i = 1; i < a.length - 1; i++) {
                sum = sum + a[i];
            }
            score = sum / (a.length - 2);

        }
        return score;           //返回平均值
    }

}
