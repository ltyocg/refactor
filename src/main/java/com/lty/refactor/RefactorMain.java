package com.lty.refactor;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.lty.refactor.printer.MyPrintVisitor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RefactorMain {
    public static void main(String[] args) {
        FileUtils.listFiles(new File("E:\\workplace\\QiQiHaEr\\Inspur.Dzzw.CloudAccept_Pro\\src\\main\\java"), new String[]{"java"}, true).parallelStream().forEach(file -> {
            CompilationUnit compilationUnit = refactor(file, new ControllerRefactor());
            PrettyPrinterConfiguration prettyPrinterConfiguration = new PrettyPrinterConfiguration().setVisitorFactory(MyPrintVisitor::new);
            if (compilationUnit != null) {
                try {
                    FileUtils.write(file, compilationUnit.toString(prettyPrinterConfiguration), StandardCharsets.UTF_8);
                    System.out.println("Done: " + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static CompilationUnit refactor(File file, Refactor refactor) {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(file);
            if (refactor.execute(compilationUnit)) {
                return compilationUnit;
            }
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
