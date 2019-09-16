package com.lty.dictionary;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Set<String> wordSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        PrettyPrinterConfiguration prettyPrinterConfiguration = new PrettyPrinterConfiguration();
        //去除注释
        prettyPrinterConfiguration.setPrintComments(false);
        FileUtils.listFiles(new File("/Users/ltyocg/Documents/GitHub/spring-framework/spring-core/src/main/java/org/springframework/core/serializer/support"), new String[]{"java"}, true).stream()
                .filter(file -> !"package-info.java".equals(file.getName()))
                .forEach(file -> {
                    System.out.println("filename: " + file.getName());
                    try {
                        CompilationUnit compilationUnit = StaticJavaParser.parse(file);
                        //去除包声明
                        compilationUnit.removePackageDeclaration();
                        //去除引入
                        compilationUnit.setImports(new NodeList<>());
                        String content = compilationUnit.toString(prettyPrinterConfiguration).replaceAll("[+\\-*/=()\\[\\]&|!^@.,<>?;'\"%\\d]", " ");
                        Arrays.stream(content.split(System.getProperty("line.separator")))
                                .filter(StringUtils::isNotBlank)
                                .forEach(line -> wordSet.addAll(Arrays.stream(line.split("\\s+"))
                                        .map(StringUtils::splitByCharacterTypeCamelCase)
                                        .flatMap(Arrays::stream)
                                        //过滤无效单词
                                        .filter(word -> word.length() > 1)
                                        .collect(Collectors.toCollection(TreeSet::new))));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                });
        //过滤Java关键字
        wordSet.removeIf(word -> {
            for (String key : new String[]{
                    //访问控制
                    "private", "protected", "public",
                    //类、方法和变量修饰符
                    "abstract", "class", "extends", "final", "implements", "interface", "native", "new", "static", "strictfp", "synchronized", "transient", "volatile",
                    //程序控制语句
                    "break", "case", "continue", "default", "do", "else", "for", "if", "instanceof", "return", "switch", "while",
                    //错误处理
                    "assert", "catch", "finally", "throw", "throws", "try",
                    //包相关
                    "import", "package",
                    //基本类型
                    "boolean", "byte", "char", "double", "float", "int", "long", "short",
                    //变量引用
                    "super", "this", "void",
                    //保留关键字
                    "null", "goto", "const"}) {
                if (key.equalsIgnoreCase(word)) {
                    return true;
                }
            }
            return false;
        });
        System.out.println(wordSet.toString());
    }
}
