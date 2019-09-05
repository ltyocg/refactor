package com.lty.refactor;

import com.github.javaparser.ast.CompilationUnit;

/**
 * 重构器
 */
public interface Refactor {
    /**
     * 执行操作
     *
     * @param compilationUnit
     */
    void execute(CompilationUnit compilationUnit);
}
