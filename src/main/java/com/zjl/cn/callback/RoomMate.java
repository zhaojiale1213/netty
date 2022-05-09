package com.zjl.cn.callback;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

// 室友类
public class RoomMate {

    public String getAnswer(String homework) {
        if ("1+1=?".equals(homework)) {
            return "2";
        } else {
            return null;
        }
    }


    public void getAnswer(String homework, DoHomeWork someone) {
        if ("1+1=?".equals(homework)) {
            someone.doHomeWork(homework, "2");
        } else if ("当x趋向于0，sin(x)/x =?".equals(homework)) {
            System.out.print("思考：");
            try {
                TimeUnit.SECONDS.sleep(2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            someone.doHomeWork(homework, "1");
        } else {
            someone.doHomeWork(homework, "(空白)");
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        new RoomMate().getAnswer("1+1=?", new DoHomeWork() {
            @Override
            public void doHomeWork(String question, String answer) {
                System.out.println("问题："+question+" 答案："+answer);
            }
        });
        Class<?> clazz = Class.forName("com.zjl.cn.callback.DoHomeWork");
        System.out.println(clazz);
        // 判断是否是接口
        System.out.println(clazz.isInterface());
        // 判断是否是匿名内部类
        System.out.println(clazz.isAnonymousClass());
        // 通过修饰符判断是否是抽象类
        int modifiers = clazz.getModifiers();
        boolean anAbstract = Modifier.isAbstract(modifiers);
        System.out.println(anAbstract);
    }
}
