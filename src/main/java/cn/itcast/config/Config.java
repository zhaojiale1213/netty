package cn.itcast.config;

import cn.itcast.protocol.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/4/6 22:25
 * @Modified By:
 */
@Slf4j
public abstract class Config {

    static Properties properties;

    static {
        try(InputStream in = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }


    public static int getServerPort() {
        String port = properties.getProperty("", "8080");
        return Integer.parseInt(port);
    }

    public static Serializer.Algorithm getSerializerAlgorithm() {
        String value = properties.getProperty("serializer.algorithm");
        if(value == null) {
            return Serializer.Algorithm.JAVA;
        } else {
            return Serializer.Algorithm.valueOf(value);
        }
    }

}
