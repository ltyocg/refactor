package com.lty.refactor.printer;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.io.File;
import java.io.FileNotFoundException;

public class LexicalPreserving {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("E:\\workplace\\Inspur.Dzzw.CloudAccept_Pro\\src\\main\\java\\com\\inspur\\sso\\MainController.java");
        CompilationUnit compilationUnit = StaticJavaParser.parse(file);
        LexicalPreservingPrinter.setup(compilationUnit);
        System.out.println(LexicalPreservingPrinter.print(compilationUnit));
    }
}
