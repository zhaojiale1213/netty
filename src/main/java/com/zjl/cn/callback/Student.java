package com.zjl.cn.callback;

public class Student implements DoHomeWork {

    /**
     * 室友做给答案
     * @param homeWork
     * @param answer
     */
    @Override
    public void doHomeWork(String homeWork, String answer) {
        System.out.println("作业本");
        if (answer != null) {
            System.out.println("作业：" + homeWork + " 答案：" + answer);
        } else {
            System.out.println("作业：" + homeWork + " 答案：" + "(空白)");
        }
    }


    public void ask(final String homework, final RoomMate roomMate) {
        /** 此线程在这等待 */
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("this指：" + this.getClass().getName() + "谁来调用ask方法");
                /** Student.this  内部类调用外部类时限定使用的对象 */
                roomMate.getAnswer(homework, Student.this);
            }
        }).start();

        goHome();
    }

    public void goHome(){
        System.out.println("我回家了……好室友，帮我写下作业。");
    }

    public static void main(String[] args) {
        Student student = new Student();

        String aHomeWork = "1+1=?";

        RoomMate roomMate = new RoomMate();

        roomMate.getAnswer(aHomeWork, new DoHomeWork() {
            @Override
            public void doHomeWork(String question, String answer) {
                System.out.println("作业：" + question + " 答案：" + answer);
            }
        });

        System.out.println("+++++++++++++++++++++++++++++");

        String homework = "当x趋向于0，sin(x)/x =?";
        student.ask(homework, new RoomMate());

    }
}
