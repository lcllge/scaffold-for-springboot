package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @version V1.0.0
 * @ClassName: {@link JarPackageScanner}
 * @Description: Jar包内置包扫描器
 * @author: 兰州
 * @date: 2019/7/16 10:12
 * @Copyright:2019 All rights reserved.
 */
@Slf4j
public class JarPackageScanner {

    private String basePackage;
    private ClassLoader cl;

    /**
     * 初始化
     *
     * @param basePackage
     */
    public JarPackageScanner(String basePackage) {
        this.basePackage = basePackage;
        this.cl = this.getClass().getClassLoader();
    }

    public JarPackageScanner(String basePackage, ClassLoader cl) {
        this.basePackage = basePackage;
        this.cl = cl;
    }

    /**
     * 获取指定包下的所有字节码文件的全类名
     */
    public List<String> getFullyQualifiedClassNameList() throws IOException {
        log.debug("scan classes base on package: {}", basePackage);
        return doScan(basePackage, new ArrayList<>());
    }

    /**
     * doScan函数
     *
     * @param basePackage
     * @param nameList
     * @return
     * @throws IOException
     */
    private List<String> doScan(String basePackage, List<String> nameList) throws IOException {
        String splashPath = StringUtil.dotToSplash(basePackage);
        //file:/D:/WorkSpace/java/ScanTest/target/classes/com/scan
        URL url = cl.getResource(splashPath);
        String filePath = StringUtil.getRootPath(url);
        // contains the name of the class file. e.g., Apple.class will be stored as "Apple"
        List<String> names = null;
        // 先判断是否是jar包，如果是jar包，通过JarInputStream产生的JarEntity去递归查询所有类
        if (isJarFile(filePath)) {
            if (log.isDebugEnabled()) {
                log.debug("{} 是一个JAR包", filePath);
            }
            names = readFromJarFile(filePath, splashPath);
            return scanJarClassPath(names);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("{} 是一个目录", filePath);
            }
            names = readFromDirectory(filePath);
        }
        for (String name : names) {
            if (isClassFile(name)) {
                nameList.add(toFullyQualifiedName(name, basePackage));
            } else {
                doScan(basePackage + "." + name, nameList);
            }
        }
        if (log.isDebugEnabled()) {
            for (String n : nameList) {
                log.debug("找到{}", n);
            }
        }
        return nameList;
    }

    private List<String> scanJarClassPath(List<String> names) {
        List<String> result = new ArrayList<>();
        for (String name : names) {
            result.add(StringUtil.trimJarPath(name));
        }
        return result;
    }

    private String toFullyQualifiedName(String shortName, String basePackage) {
        StringBuilder sb = new StringBuilder(basePackage);
        sb.append('.');
        sb.append(StringUtil.trimExtension(shortName));
        log.debug("the current class info: {}", sb.toString());
        return sb.toString();
    }

    private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("从JAR包中读取类: {}", jarPath);
        }
        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jarIn.getNextJarEntry();
        List<String> nameList = new ArrayList<String>();
        while (null != entry) {
            String name = entry.getName();
            if (name.startsWith(splashedPackageName) && isClassFile(name)) {
                nameList.add(name);
            }

            entry = jarIn.getNextJarEntry();
        }
        return nameList;
    }

    private List<String> readFromDirectory(String path) {
        File file = new File(path);
        String[] names = file.list();

        if (null == names) {
            return null;
        }

        return Arrays.asList(names);
    }

    private boolean isClassFile(String name) {
        return name.endsWith(".class");
    }

    private boolean isJarFile(String name) {
        return name.endsWith(".jar");
    }

}
