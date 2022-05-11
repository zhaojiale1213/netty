package com.zjl.cn.design.strategy.example3;

public enum ParcelCompanyEnum {

    ZTO("中通快递"),YTO("圆通快递"),STO("申通快递"),JD("京东快递");

    String name;

    ParcelCompanyEnum(String name) {
        this.name = name;
    }

    /**
     * if else - 优化使用策略模式
     * @param company
     * @param weight
     * @return
     */
    public Double calculatePostage1(ParcelCompanyEnum company, Integer weight) {
        if (company == ParcelCompanyEnum.JD) {
            return 10 + weight * 1.2;
        } else if (company == ParcelCompanyEnum.STO) {
            return 12 + weight * 0.8;
        } else if (company == ParcelCompanyEnum.YTO) {
            return 8 + weight * 1.5;
        } else if (company == ParcelCompanyEnum.ZTO) {
            return 9 + weight * 1.1;
        } else {
            throw new IllegalArgumentException("No such company :" + company);
        }
    }
}
