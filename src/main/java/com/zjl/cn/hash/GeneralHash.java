package com.zjl.cn.hash;

/**
 * @Description: 普通hash算法
 * @Author: zjl
 * @Date:Created in 2022/4/27 21:45
 * @Modified By:
 */
public class GeneralHash {

    public static void main(String[] args) {
        // 定义客户端IP
        String[] clients = new String[] {"10.78.12.3","113.25.63.1","126.12.3.8","180.156.10.15"};
        // 定义服务器数量
        int serverCount = 3;// (编号对应0，1，2)
        for (String client : clients) {
            int hash = Math.abs(client.hashCode());
            int index = hash % serverCount;
            System.out.println("客户端：" + client + " 被路由到服务器编号为：" + index);
        }
    }

}
