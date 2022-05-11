package com.zjl.cn.design.strategy.example3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CalculatePostage {

    private static Map<ParcelCompanyEnum, Function<Integer, Double>> map = new HashMap<>(5);

    static {
        map.put(ParcelCompanyEnum.JD, new Function<Integer, Double>() {
            @Override
            public Double apply(Integer weight) {
                return calculateJd(weight);
            }
        });
        map.put(ParcelCompanyEnum.STO, new Function<Integer, Double>() {
            @Override
            public Double apply(Integer weight) {
                return calculatSto(weight);
            }
        });
        map.put(ParcelCompanyEnum.YTO, CalculatePostage::calculateYto);
        map.put(ParcelCompanyEnum.ZTO, CalculatePostage::calculateZto);
    }

    public static Double calculateJd(Integer weight) {
        return 10 + weight * 1.2;
    }
    public static Double calculatSto(Integer weight) {
        return 12 + weight * 0.8;
    }
    public static Double calculateYto(Integer weight) {
        return 8 + weight * 1.5;
    }
    public static Double calculateZto(Integer weight) {
        return 9 + weight * 1.1;
    }

    public static Map<ParcelCompanyEnum, Function<Integer, Double>> getMap() {
        return map;
    }

    public static Double calculatePostage4(ParcelCompanyEnum companyEnum, Integer weight) {
        return getMap().get(companyEnum).apply(weight);
    }
}
