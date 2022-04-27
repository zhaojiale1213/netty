package com.zjl.cn.hash;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Description: ⼀致性Hash算法实现（不含虚拟节点）
 * @Author: zjl
 * @Date:Created in 2022/4/27 21:53
 * @Modified By:
 */
public class ConsistentHashNoVirtual {

    public static void main(String[] args) {
        //step1 初始化：把服务器节点IP的哈希值对应到哈希环上
        // 定义服务器ip
        String[] tomcatServers = new String[] {"123.111.0.0","123.101.3.1","111.20.35.2","123.98.26.3"};
        // 求出每一个ip对应的hash值，对应到hash环上，存储hash -> ip的对应关系
        SortedMap<Integer, String> hashServerMap = new TreeMap<>();
        for (String tomcatServer : tomcatServers) {
            int serverHash = Math.abs(tomcatServer.hashCode());
            hashServerMap.put(serverHash, tomcatServer);
        }

        //step2 针对客户端IP求出hash值
        // 定义客户端IP
        String[] clients = new String[] {"10.78.12.3","113.25.63.1","126.12.3.8"};
        for (String client : clients) {
            int clientHash = Math.abs(client.hashCode());
            // 根据客户端ip的哈希值去找出哪⼀个服务器节点能够处理
            SortedMap<Integer, String> tailMap = hashServerMap.tailMap(clientHash);
            if (tailMap.isEmpty()) {
                // 取哈希环上的顺时针第⼀台服务器
                Integer firstKey = hashServerMap.firstKey();
                System.out.println("==========>>>>客户端：" + client + " 被路由到服务器："
                    + hashServerMap.get(firstKey));
            } else {
                Integer firstKey = tailMap.firstKey();
                System.out.println("==========>>>>客户端：" + client + " 被路由到服务器："
                    + hashServerMap.get(firstKey));
            }
        }
    }
}
