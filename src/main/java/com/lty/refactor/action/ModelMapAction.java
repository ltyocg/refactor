package com.lty.refactor.action;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import com.lty.refactor.utils.AnnotationUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class ModelMapAction {
    public static void main(String[] args) {
        FileUtils.listFiles(new File("E:\\workplace\\Inspur.Dzzw.CloudAccept_Pro\\src\\main\\java"), new String[]{"java"}, true).parallelStream().forEach(file -> {
            try {
                CompilationUnit compilationUnit = StaticJavaParser.parse(file);
                LexicalPreservingPrinter.setup(compilationUnit);
                compilationUnit.findAll(ClassOrInterfaceDeclaration.class, classOrInterfaceDeclaration -> AnnotationUtils.contains(classOrInterfaceDeclaration.getAnnotations(), "Controller") && !classOrInterfaceDeclaration.isAbstract())
                        .forEach(classOrInterfaceDeclaration -> {
                            classOrInterfaceDeclaration.accept(new VoidVisitorAdapter<Object>() {
                            }, null);
                        });
                String codeContent = LexicalPreservingPrinter.print(compilationUnit);
                FileUtils.write(file, codeContent, StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
