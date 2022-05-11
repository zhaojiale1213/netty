package com.zjl.cn.design.strategy.example3;

import java.util.HashMap;
import java.util.Map;

public class CalculateFactory {
    private static Map<ParcelCompanyEnum, CalculateStrategy> map = new HashMap<>(5);

    static {
        map.put(ParcelCompanyEnum.JD, new CalculateStrategyJd());
        map.put(ParcelCompanyEnum.YTO, new CalculateStrategyYto());
        map.put(ParcelCompanyEnum.ZTO, new CalculateStrategyZto());
        map.put(ParcelCompanyEnum.STO, new CalculateStrategySto());
    }

    public static CalculateStrategy creat(ParcelCompanyEnum company) {
        return map.get(company);
    }

    /**
     * 提供计算方法
     * @param company
     * @param weight
     * @return
     */
    public static Double calculatePostage3(ParcelCompanyEnum company, Integer weight) {
        return CalculateFactory.creat(company).calculate(weight);
    }
}
