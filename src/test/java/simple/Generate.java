package simple;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.github.javaparser.ast.Modifier.Keyword.*;

/**
 * 生成
 */
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

    @Test
    void myTest() {
        CompilationUnit compilationUnit = new CompilationUnit();
        ClassOrInterfaceDeclaration myClass = compilationUnit
                .addClass("MyClass")
                .setPublic(true);
        myClass.addField(int.class, "A_CONSTANT", PUBLIC, STATIC);
        myClass.addField(String.class, "name", PRIVATE);
        myClass.addField(LocalDateTime.class, "time", PRIVATE);
        String code = myClass.toString();
        System.out.println(code);
    }
}
