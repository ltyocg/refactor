package com.lty.refactor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@code Controller}重构器
 */
public class ControllerRefactor extends Refactor {
    @Override
    public boolean execute(Node node) {
        AtomicBoolean state = new AtomicBoolean(false);
        node
                //查找BaseController的子类
                .findAll(ClassOrInterfaceDeclaration.class, classOrInterfaceDeclaration -> !"AbstractController".equals(classOrInterfaceDeclaration.getNameAsString()))
                .forEach(classOrInterfaceDeclaration -> {
                    for (ClassOrInterfaceType extendedType : classOrInterfaceDeclaration.getExtendedTypes()) {
                        if ("BaseController".equals(extendedType.asString())) {
                            extendedType.setName("AbstractController");
                            state.set(true);
                        }
                    }
                });
        if (state.get()) {
            ((CompilationUnit) node).getImports().add(new ImportDeclaration("com.inspur.mng.core.action.AbstractController", false, false));
        }
        return state.get();
    }
}
