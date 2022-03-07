package com.itcast.nio.c1;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 遍历文件
 * @Author: zjl
 * @Date:Created in 2022/2/21 21:16
 * @Modified By:
 */
public class TestWalkFileTree {


    @Test
    public void test1() {
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        try {
            Files.walkFileTree(Paths.get("C:\\Program Files (x86)\\Java\\jdk1.8.0_231"), new SimpleFileVisitor<Path>() {
                /** 进入文件夹前 */
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    System.out.println("--------> 进入 dir: " + dir);
                    dirCount.incrementAndGet();
//                    final File file = dir.toFile();
                    return super.preVisitDirectory(dir, attrs);
                }

                /** 查看文件 */
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println("file: " + file);
                    fileCount.incrementAndGet();
                    return super.visitFile(file, attrs);
                }

                /** 查看文件失败 */
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return super.visitFileFailed(file, exc);
                }

                /** 退出文件夹后 */
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    System.out.println("<-------- 退出 dir: " + dir);
                    return super.postVisitDirectory(dir, exc);
                }
            });
            System.out.println("dirCount: " + dirCount.get());
            System.out.println("fileCount: " + fileCount.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test2() throws IOException {
        AtomicInteger jarCount = new AtomicInteger();
        Files.walkFileTree(Paths.get("C:\\Program Files (x86)\\Java\\jdk1.8.0_231"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                if (path.toString().endsWith(".jar")) {
                    System.out.println(path);
                    jarCount.incrementAndGet();
                }
                return super.visitFile(path, attrs);
            }
        });
        System.out.println("jarCount: " + jarCount.get());
    }


    /** 删除文件夹，先删除里面的文件，再删除文件夹 */
    @Test
    public void test3() throws IOException {
        Files.walkFileTree(Paths.get("D:\\Tools\\vpn - 副本"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(Files.isRegularFile(file));
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println(Files.isDirectory(dir));
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }

}
