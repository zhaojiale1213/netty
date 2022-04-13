package com.zjl.cn.Callback;

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

    public static void main(String[] args) {
        new RoomMate().getAnswer("1+1=?", new DoHomeWork() {
            @Override
            public void doHomeWork(String question, String answer) {
                System.out.println("问题："+question+" 答案："+answer);
            }
        });
    }
}
