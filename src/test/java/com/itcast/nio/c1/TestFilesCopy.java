package com.itcast.nio.c1;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Description: 文件夹copy
 * @Author: zjl
 * @Date:Created in 2022/2/21 22:33
 * @Modified By:
 */
public class TestFilesCopy {


    @Test
    public void test() throws IOException {
        String source = "D:\\Tools\\jmeter";
        String target = "D:\\Tools\\jmeter222";

        Files.walk(Paths.get(source)).forEach(path -> {
            try {
                String targetName = path.toString().replace(source, target);
                if (Files.isDirectory(path)) {
                    Files.createDirectory(Paths.get(targetName));
                } else if (Files.isRegularFile(path)) {
                    Files.copy(path, Paths.get(targetName));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
