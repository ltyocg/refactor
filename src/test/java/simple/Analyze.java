package simple;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.junit.jupiter.api.Test;

/**
 * 分析
 */
class Analyze {
    /**
     * 官方示例
     */
    @Test
    void analyze() {
        CompilationUnit compilationUnit = StaticJavaParser.parse("class A { }");
        compilationUnit.findAll(FieldDeclaration.class).stream()
                .filter(f -> f.isPublic() && !f.isStatic())
                .forEach(f -> System.out.println("Check field at line " +
                        f.getRange().map(r -> r.begin.line).orElse(-1)));
    }

    @Test
    void analysis() {
        // Set up a minimal type solver that only looks at the classes used to run this sample.
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());

        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        // Parse some code
        CompilationUnit cu = StaticJavaParser.parse("class X { int x() { return 1 + 1.0 - 5; } }");

        // Find all the calculations with two sides:
        cu.findAll(BinaryExpr.class).forEach(be -> {
            // Find out what type it has:
            ResolvedType resolvedType = be.calculateResolvedType();

            // Show that it's "double" in every case:
            System.out.println(be.toString() + " is a: " + resolvedType);
        });
    }
}
