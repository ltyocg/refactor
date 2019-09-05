package com.lty.refactor;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;

public class RefactorMain {
    public static void main(String[] args) {
        File file = new File("E:\\workplace\\QiQiHaEr\\Inspur.Dzzw.CloudAccept_Pro\\src\\main\\java\\com\\inspur\\accept\\systemsetting\\message\\action\\SendMessageController.java");
    }

    private static CompilationUnit refactor(File file, Refactor refactor) {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(file);
            refactor.execute(compilationUnit);
            return compilationUnit;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
