package com.zjl.cn.aio;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/2/2 10:59
 * @Modified By:
 */
public class A {


    public static void main(String[] args) {
        int a = 1;

        try {
            int b = 1/0;
        } catch (Exception e) {
            System.out.println("11");
            System.out.println(a);
        } finally {
            ++a;
            System.out.println("finally" + a);
        }

        System.out.println("--------");
        System.out.println(test());
    }


    public static int test() {
        int a = 1;

        try {
            int b = 1/0;
            return a;
        } catch (Exception e) {
            System.out.println("11");
            return a;
        } finally {
            ++a;
            System.out.println("?: " + a);
        }
    }

}
