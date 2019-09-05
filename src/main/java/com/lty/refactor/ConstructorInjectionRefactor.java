package com.lty.refactor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 将原来的{@code @Autowired}注入改成构造函数注入
 */
public class ConstructorInjectionRefactor implements Refactor {
    @Override
    public void execute(CompilationUnit compilationUnit) {
        compilationUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(classOrInterfaceDeclaration -> {
            List<VariableDeclarator> fieldList = new ArrayList<>();
            classOrInterfaceDeclaration
                    .findAll(FieldDeclaration.class, fieldDeclaration -> {
                        for (AnnotationExpr annotation : fieldDeclaration.getAnnotations()) {
                            if ("Autowired".equals(annotation.getNameAsString())) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .forEach(fieldDeclaration -> {
                        fieldList.add(fieldDeclaration.getVariable(0));
                        //设为final
                        fieldDeclaration.setFinal(true);
                        //去除@Autowired
                        fieldDeclaration.setAnnotations(fieldDeclaration.getAnnotations().stream()
                                .filter(annotationExpr -> !"Autowired".equals(annotationExpr.getName().asString())).collect(Collectors.toCollection(NodeList::new)));
                    });
            ConstructorDeclaration constructorDeclaration = classOrInterfaceDeclaration.addConstructor(Modifier.Keyword.PUBLIC);
            //成员排序
            classOrInterfaceDeclaration.getMembers().sort(Comparator.comparing(o -> {
                if (o instanceof FieldDeclaration) {
                    return 1;
                } else if (o instanceof ConstructorDeclaration) {
                    return 2;
                } else if (o instanceof MethodDeclaration) {
                    return 3;
                } else {
                    return 9;
                }
            }));
            //MarkAnnotation不带空括号
            constructorDeclaration.addMarkerAnnotation("Autowired");
            fieldList.stream()
                    .map(variableDeclarator -> new Parameter(variableDeclarator.getType(), variableDeclarator.getNameAsString()))
                    .forEach(constructorDeclaration::addParameter);
            constructorDeclaration.setBody(new BlockStmt(fieldList.stream()
                    .map(variableDeclarator -> new ExpressionStmt(new AssignExpr(new FieldAccessExpr(new ThisExpr(), variableDeclarator.getNameAsString()), new NameExpr(variableDeclarator.getNameAsString()), AssignExpr.Operator.ASSIGN)))
                    .collect(Collectors.toCollection(NodeList::new))));
        });
    }
}
