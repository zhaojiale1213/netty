package com.zjl.cn.util;

import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 用于获取指定包名下的所有类名.
 */
public class ClassUtil {

    public static void main(String[] args) throws ClassNotFoundException {

        Class<?> c = Class.forName("cn.hutool.aop.aspects.Aspect");
        System.out.println(c);
        // 标识是否要遍历该包路径下子包的类名
        boolean recursive = true;
        // 指定的包名
        String pkg = "cn.hutool.aop.aspects";
//        String pkg = "com.zjl.cn.callback";

        List<Class<?>> classList = getClassList(pkg, recursive, null);
        System.out.println("=====================");
        for (Class<?> clazz : classList) {
            System.out.println(clazz.getName());
        }
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
                    // 真实路径 pkgPath: /D:/zjl/netty/target/classes/com/zjl/cn/callback
                    String pkgPath = url.getPath();
                    System.out.println("protocol:" + protocol +"--- path:" + pkgPath);
                    if ("file".equalsIgnoreCase(protocol)) {
                        // 本地自己可见的代码
                        findClassName(classList, pkgName, pkgPath, isRecursive, annotation);
                    } else if ("jar".equalsIgnoreCase(protocol)) {
                        // 引用第三方jar的代码
                        findJarClassName(classList, pkgName, url, isRecursive, annotation);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return classList;
    }

    private static void findClassName(List<Class<?>> clazzList, String pkgName, String pkgPath,
                                     boolean isRecursive, Class<? extends Annotation> annotation) {
        File[] files = filterClassFiles(pkgPath);
        if (null == files || files.length == 0) return;
        for (File file : files) {
            // 文件 fileName = DoHomeWork.class  文件夹 test
            String fileName = file.getName();
            if (file.isFile()) {
                String className = getClassName(pkgName, fileName);
                addClassName(clazzList, className, annotation);
            } else if (file.isDirectory()) {
                // 需要继续查找该文件夹/包名下的类
                if (isRecursive) {
                    String subPkgName = pkgName + "." + fileName;
                    String subPkgPath = pkgPath + "/" + fileName;
                    findClassName(clazzList, subPkgName, subPkgPath, true, annotation);
                }
            }
        }
    }


    private static void addClassName(List<Class<?>> clazzList, String clazzName,
                                     Class<? extends Annotation> annotation) {
        if (clazzList != null && clazzName != null) {
            try {
                Class<?> clazz = Class.forName(clazzName);
                if(annotation == null){
                    clazzList.add(clazz);
                    System.out.println("add:" + clazz);
                } else if (clazz.isAnnotationPresent(annotation)){
                    clazzList.add(clazz);
                    System.out.println("add annotation:" + clazz);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("clazzName: " + clazzName);
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取class文件的包名+类名
     * @param pkgName
     * @param fileName
     * @return
     */
    private static String getClassName(String pkgName, String fileName) {
        int index = fileName.lastIndexOf(".");
        String clazz = null;
        if (index >= 0) clazz = fileName.substring(0, index);
        String clazzName = null;
        if (clazz != null) clazzName = pkgName + "." + clazz;
        return clazzName;
    }

    /**
     * 第三方Jar类库的引用。<br/>
     * @throws IOException
     * */
    public static void findJarClassName(List<Class<?>> clazzList, String pkgName, URL url, boolean isRecursive,
                                     Class<? extends Annotation> annotation) throws IOException {
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        System.out.println("jarFile:" + jarFile.getName());
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            // 类似：sun/security/internal/interfaces/TlsMasterSecret.class
            String jarEntryName = jarEntry.getName();
            String clazzName = jarEntryName.replace("/", ".");
            int endIndex = clazzName.lastIndexOf(".");
            String prefix = null;
            if (endIndex > 0) {
                String prefix_name = clazzName.substring(0, endIndex);
                endIndex = prefix_name.lastIndexOf(".");
                if(endIndex > 0){
                    prefix = prefix_name.substring(0, endIndex);
                }
            }
            if (prefix != null && jarEntryName.endsWith(".class")) {
                if (prefix.equals(pkgName)) {
                    System.out.println("jar entryName:" + jarEntryName);
                    addClassName(clazzList, clazzName, annotation);
                } else if (isRecursive && prefix.startsWith(pkgName)) {
                    // 遍历子包名：子类
                    System.out.println("jar entryName:" + jarEntryName +" isRecursive:" + isRecursive);
                    addClassName(clazzList, clazzName, annotation);
                }
            }
        }
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
