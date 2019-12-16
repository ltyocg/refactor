package com.lty.refactor.action;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import com.lty.refactor.utils.AnnotationUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConstructorInjectionMain {
    public static void main(String[] args) {
        FileUtils.listFiles(new File("E:\\workplace\\Inspur.Dzzw.CloudAccept_Pro\\src\\main\\java"), new String[]{"java"}, true).parallelStream().forEach(file -> {
            try {
                CompilationUnit compilationUnit = StaticJavaParser.parse(file);
                LexicalPreservingPrinter.setup(compilationUnit);
                compilationUnit.findAll(ClassOrInterfaceDeclaration.class, classOrInterfaceDeclaration -> AnnotationUtils.contains(classOrInterfaceDeclaration.getAnnotations(), "Controller", "Service") && !classOrInterfaceDeclaration.isAbstract())
                        .forEach(classOrInterfaceDeclaration -> {
                            List<FieldDeclaration> fieldList = new ArrayList<>();
                            classOrInterfaceDeclaration
                                    .findAll(FieldDeclaration.class, fieldDeclaration -> AnnotationUtils.contains(fieldDeclaration.getAnnotations(), "Autowired"))
                                    .forEach(fieldDeclaration -> {
                                        fieldList.add(fieldDeclaration);
                                        //去除@Autowired
                                        fieldDeclaration.getAnnotations()
                                                .removeIf(annotationExpr -> "Autowired".equals(annotationExpr.getName().asString()));
                                        fieldDeclaration.setFinal(true);
                                    });
                            if (classOrInterfaceDeclaration.getConstructors().size() == 0 && !fieldList.isEmpty()) {
                                ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration();
                                constructorDeclaration.setModifiers(Modifier.createModifierList(Modifier.Keyword.PUBLIC));
                                constructorDeclaration.setName(classOrInterfaceDeclaration.getName());
                                //MarkAnnotation不带空括号
                                constructorDeclaration.addMarkerAnnotation("Autowired");
                                List<VariableDeclarator> variableList = fieldList.stream().map(field -> field.getVariable(0)).collect(Collectors.toList());
                                variableList.stream()
                                        .map(variableDeclarator -> new Parameter(variableDeclarator.getType(), variableDeclarator.getNameAsString()))
                                        .forEach(constructorDeclaration::addParameter);
                                constructorDeclaration.setBody(new BlockStmt(variableList.stream()
                                        .map(variableDeclarator -> new ExpressionStmt(new AssignExpr(new FieldAccessExpr(new ThisExpr(), variableDeclarator.getNameAsString()), new NameExpr(variableDeclarator.getNameAsString()), AssignExpr.Operator.ASSIGN)))
                                        .collect(Collectors.toCollection(NodeList::new))));
                                //构造函数放置在最后一个类属性的后面
                                int constructorIndex = classOrInterfaceDeclaration.getMembers().lastIndexOf(fieldList.get(fieldList.size() - 1)) + 1;
                                classOrInterfaceDeclaration.getMembers().add(constructorIndex, constructorDeclaration);
                            }
                        });
                String codeContent = LexicalPreservingPrinter.print(compilationUnit);
                FileUtils.write(file, codeContent, StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
