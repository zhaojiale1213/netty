package com.zjl.cn.util;

import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 用于获取指定包名下的所有类名.
 */
public class ClassUtil {

    public static void main(String[] args) {
        // 标识是否要遍历该包路径下子包的类名
        boolean recursive = true;
        // 指定的包名
        String pkg = "javax.crypto.spec";


    }

    public static List<Class<?>> getClassList(String pkgName , boolean isRecursive,
                                              Class<? extends Annotation> annotation) {
        List<Class<?>> classList = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            String strFile = pkgName.replaceAll("\\.", "/");
            // 参数要求以 "/" 分割，见源码
            Enumeration<URL> urls = loader.getResources(strFile);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    // 真实路径
                    String pkgPath = url.getPath();
                    System.out.println("protocol:" + protocol +" path:" + pkgPath);
                    if ("file".equalsIgnoreCase(protocol)) {
                        // 本地自己可见的代码
                        findClassName(classList, pkgName, pkgPath, isRecursive, annotation);
                    } else if ("jar".equalsIgnoreCase(protocol)) {
                        // 引用第三方jar的代码
                        findClassName(classList, pkgName, url, isRecursive, annotation);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return classList;
    }

    public static void findClassName(List<Class<?>> clazzList, String pkgName, String pkgPath,
                                     boolean isRecursive, Class<? extends Annotation> annotation) {
        File[] files = filterClassFiles(pkgPath);


    }

    /**
     * 第三方Jar类库的引用。<br/>
     * @throws IOException
     * */
    public static void findClassName(List<Class<?>> clazzList, String pkgName, URL url, boolean isRecursive,
                                     Class<? extends Annotation> annotation) throws IOException {


    }

    private static File[] filterClassFiles(String pkgPath) {
        if (StrUtil.isEmpty(pkgPath)) return null;
        File file = new File(pkgPath);
        // 接收 .class 文件 或 类文件夹
        return file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return (f.isFile() && f.getName().endsWith(".class")) || f.isDirectory();
            }
        });
    }

}
