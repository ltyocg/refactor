package com.lty.refactor.utils;

import com.github.javaparser.ast.expr.AnnotationExpr;

import java.util.List;

/**
 * 注释工具
 */
public final class AnnotationUtils {
    /**
     * @param annotationExprList 注释列表
     * @param annotationAsString 注释名称
     * @return 是否包含指定注释
     */
    public static boolean contains(List<AnnotationExpr> annotationExprList, String... annotationAsString) {
        for (AnnotationExpr annotation : annotationExprList) {
            if (annotationAsString.equals(annotation.getNameAsString())) {
                return true;
            }
        }
        return false;
    }
}
