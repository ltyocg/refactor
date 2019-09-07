package com.lty.refactor;

import com.github.javaparser.ast.Node;

/**
 * 重构器
 */
public interface Refactor {
    /**
     * 执行操作
     *
     * @param node
     */
    void execute(Node node);
}
