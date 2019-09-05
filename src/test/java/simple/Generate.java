package simple;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.junit.jupiter.api.Test;

import static com.github.javaparser.ast.Modifier.Keyword.*;

class Generate {
    /**
     * 官方示例
     */
    @Test
    void generate() {
        CompilationUnit compilationUnit = new CompilationUnit();
        ClassOrInterfaceDeclaration myClass = compilationUnit
                .addClass("MyClass")
                .setPublic(true);
        myClass.addField(int.class, "A_CONSTANT", PUBLIC, STATIC);
        myClass.addField(String.class, "name", PRIVATE);
        String code = myClass.toString();
        System.out.println(code);
    }
}
