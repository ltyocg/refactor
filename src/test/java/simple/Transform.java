package simple;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.junit.jupiter.api.Test;

class Transform {
    /**
     * 官方示例
     */
    @Test
    void transform() {
        CompilationUnit compilationUnit = StaticJavaParser.parse("class A { }");
        compilationUnit.findAll(ClassOrInterfaceDeclaration.class).stream()
                .filter(c -> !c.isInterface()
                        && c.isAbstract()
                        && !c.getNameAsString().startsWith("Abstract"))
                .forEach(c -> {
                    String oldName = c.getNameAsString();
                    String newName = "Abstract" + oldName;
                    System.out.println("Renaming class " + oldName + " into " + newName);
                    c.setName(newName);
                });
    }
}
