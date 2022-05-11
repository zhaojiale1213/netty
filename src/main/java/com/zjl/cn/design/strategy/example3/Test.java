package com.zjl.cn.design.strategy.example3;

import java.util.function.Function;

public class Test {

    public static void main(String[] args) {
        Double res = CalculateFactory.calculatePostage3(ParcelCompanyEnum.JD, 15);
        System.out.println(res);

        Function<Integer, Double> function = CalculatePostage.getMap().get(ParcelCompanyEnum.JD);
        Double apply = function.apply(15);
        System.out.println(apply);

        Double price = CalculatePostage.calculatePostage4(ParcelCompanyEnum.JD, 15);
        System.out.println(price);
    }

}
