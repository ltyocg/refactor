package simple;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

/**
 * 解析
 */
class Parse {
    /**
     * 官方示例
     */
    @Test
    void parsing() {
        CompilationUnit compilationUnit = StaticJavaParser.parse("class A { }");
        Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName("A");
    }

    /**
     * 解析词汇
     */
    @Test
    void t() {
        JavaParserTypeSolver javaParserTypeSolver = new JavaParserTypeSolver("E:\\workplace\\QiQiHaEr\\Inspur.Dzzw.CloudAccept_Pro\\src\\main\\java\\com\\inspur\\accept\\business\\agent\\po");
        SymbolReference<ResolvedReferenceTypeDeclaration> agentBean = javaParserTypeSolver.tryToSolveType("AgentBean");
    }

    @Test
    void body() throws FileNotFoundException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(new File(""));
    }
}
