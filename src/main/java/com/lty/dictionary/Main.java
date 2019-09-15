package com.lty.dictionary;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        PrettyPrinterConfiguration prettyPrinterConfiguration = new PrettyPrinterConfiguration();
        //去除注释
        prettyPrinterConfiguration.setPrintComments(false);
        FileUtils.listFiles(new File("/Users/ltyocg/Documents/GitHub/spring-framework/spring-core/src/main/java/org/springframework/core/serializer/support"), new String[]{"java"}, true).parallelStream()
                .filter(file -> !"package-info.java".equals(file.getName()))
                .forEach(file -> {
                    System.out.println("filename: " + file.getName());
                    try {
                        CompilationUnit compilationUnit = StaticJavaParser.parse(file);
                        //去除包声明
                        compilationUnit.removePackageDeclaration();
                        //去除引入
                        compilationUnit.setImports(new NodeList<>());
                        String content = compilationUnit.toString(prettyPrinterConfiguration);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                });
    }
}
