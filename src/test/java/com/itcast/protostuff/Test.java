package com.itcast.protostuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Test {

    @org.junit.Test
    public void test() {
        List<User> users = new ArrayList<>();
        users.add(new User("1", "张三", 10, "11"));
        users.add(new User("2", "李四", 20, "22"));
        users.add(new User("3", "王五", 15, "33"));
        users.add(new User("4", "马超", 16, "44"));

        Set<String> set = users.stream().filter(user -> user.getAge() > 15)
                .map(User::getName).collect(Collectors.toSet());
        set.forEach(s -> System.out.println(s));
    }

    @org.junit.Test
    public void test1() {
        System.out.println((int)'a');
        System.out.println((int)'z');
        System.out.println((int)'A');
        System.out.println((int)'Z');

        // 转小写 大写字符+32
        int m = 'A' + 32;
        System.out.println((char) m);

        // 转大写 小写字符-32
        int n = 'a' - 32;
        System.out.println(n);
        System.out.println((char)n);

        //  0xff:  15 * 16^0 + 15 * 16^1 =
        System.out.println(0xff);

        System.out.println(15 + 15 * 16);
    }

}
