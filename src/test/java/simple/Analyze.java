package simple;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import org.junit.jupiter.api.Test;

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

    void other(){
        JavaParser javaParser=new JavaParser();
        javaParser.parse()
    }
}
