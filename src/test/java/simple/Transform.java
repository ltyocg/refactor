package simple;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.lty.refactor.ConstructorInjectionRefactor;
import com.lty.refactor.printer.MyPrintVisitor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

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

    @Test
    void simpleFile() throws FileNotFoundException {
        File file = new File("E:\\workplace\\QiQiHaEr\\Inspur.Dzzw.CloudAccept_Pro\\src\\main\\java\\com\\inspur\\accept\\dockingcenter\\call\\po\\CallResultBean.java");
        CompilationUnit compilationUnit = StaticJavaParser.parse(file);
        boolean state = new ConstructorInjectionRefactor().execute(compilationUnit);
        if (!state) {
            compilationUnit = null;
        }
        PrettyPrinterConfiguration prettyPrinterConfiguration = new PrettyPrinterConfiguration().setVisitorFactory(MyPrintVisitor::new);
        if (compilationUnit != null) {
            System.out.println(compilationUnit.toString(prettyPrinterConfiguration));
        }
    }
}
