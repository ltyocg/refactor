package simple;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.javaparser.utils.Utils.isNullOrEmpty;
import static com.github.javaparser.utils.Utils.normalizeEolInTextBlock;
import static java.util.stream.Collectors.joining;

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
    void t0() throws FileNotFoundException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(new File("E:\\workplace\\QiQiHaEr\\Inspur.Dzzw.CloudAccept_Pro\\src\\main\\java\\com\\inspur\\accept\\base\\action\\ComponentController.java"));
    }

    @Test
    void constructors() throws IOException {
        File file = new File("E:\\workplace\\QiQiHaEr\\Inspur.Dzzw.CloudAccept_Pro\\src\\main\\java\\com\\inspur\\accept\\systemsetting\\message\\action\\SendMessageController.java");
        CompilationUnit compilationUnit = StaticJavaParser.parse(file);
        compilationUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(classOrInterfaceDeclaration -> {
            List<VariableDeclarator> fieldList = new ArrayList<>();
            classOrInterfaceDeclaration
                    .findAll(FieldDeclaration.class, fieldDeclaration -> {
                        for (AnnotationExpr annotation : fieldDeclaration.getAnnotations()) {
                            if ("Autowired".equals(annotation.getNameAsString())) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .forEach(fieldDeclaration -> {
                        fieldList.add(fieldDeclaration.getVariable(0));
                        //设为final
                        fieldDeclaration.setFinal(true);
                        //去除注释
                        fieldDeclaration.setAnnotations(fieldDeclaration.getAnnotations().stream()
                                .filter(annotationExpr -> !"Autowired".equals(annotationExpr.getName().asString())).collect(Collectors.toCollection(NodeList::new)));
                    });
            ConstructorDeclaration constructorDeclaration = classOrInterfaceDeclaration.addConstructor(Modifier.Keyword.PUBLIC);
            //成员排序
            classOrInterfaceDeclaration.getMembers().sort(Comparator.comparing(o -> {
                if (o instanceof FieldDeclaration) {
                    return 1;
                } else if (o instanceof ConstructorDeclaration) {
                    return 2;
                } else if (o instanceof MethodDeclaration) {
                    return 3;
                } else {
                    return 9;
                }
            }));
            //MarkAnnotation不带空括号
            constructorDeclaration.addMarkerAnnotation("Autowired");
            fieldList.stream()
                    .map(variableDeclarator -> new Parameter(variableDeclarator.getType(), variableDeclarator.getNameAsString()))
                    .forEach(constructorDeclaration::addParameter);
            constructorDeclaration.setBody(new BlockStmt(fieldList.stream()
                    .map(variableDeclarator -> new ExpressionStmt(new AssignExpr(new FieldAccessExpr(new ThisExpr(), variableDeclarator.getNameAsString()), new NameExpr(variableDeclarator.getNameAsString()), AssignExpr.Operator.ASSIGN)))
                    .collect(Collectors.toCollection(NodeList::new))));
        });
        //格式化
        PrettyPrinterConfiguration prettyPrinterConfiguration = new PrettyPrinterConfiguration().setVisitorFactory(c -> new PrettyPrintVisitor(c) {
            /**
             * @param n 构造函数
             * @param arg
             */
            @Override
            public void visit(ConstructorDeclaration n, Void arg) {
                printComment(n.getComment(), arg);
                printMemberAnnotations(n.getAnnotations(), arg);
                printModifiers(n.getModifiers());
                printTypeParameters(n.getTypeParameters(), arg);
                if (n.isGeneric()) {
                    printer.print(" ");
                }
                n.getName().accept(this, arg);
                printer.print("(");
                if (!n.getParameters().isEmpty()) {
                    for (final Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext(); ) {
                        final Parameter p = i.next();
                        p.accept(this, arg);
                        if (i.hasNext()) {
                            printer.print(", ");
                            printer.println();
                        }
                    }
                }
                printer.print(")");
                if (!isNullOrEmpty(n.getThrownExceptions())) {
                    printer.print(" throws ");
                    for (final Iterator<ReferenceType> i = n.getThrownExceptions().iterator(); i.hasNext(); ) {
                        final ReferenceType name = i.next();
                        name.accept(this, arg);
                        if (i.hasNext()) {
                            printer.print(", ");
                        }
                    }
                }
                printer.print(" ");
                n.getBody().accept(this, arg);
            }

            /**
             * @param n 行注释
             * @param arg
             */
            @Override
            public void visit(LineComment n, Void arg) {
                if (configuration.isIgnoreComments()) {
                    return;
                }
                printer
                        .print("//")
                        .println(normalizeEolInTextBlock(n.getContent(), "").trim());
            }

            private void printComment(final Optional<Comment> comment, final Void arg) {
                comment.ifPresent(c -> c.accept(this, arg));
            }

            private void printMemberAnnotations(final NodeList<AnnotationExpr> annotations, final Void arg) {
                if (annotations.isEmpty()) {
                    return;
                }
                for (final AnnotationExpr a : annotations) {
                    a.accept(this, arg);
                    printer.println();
                }
            }

            private void printModifiers(final NodeList<Modifier> modifiers) {
                if (modifiers.size() > 0) {
                    printer.print(modifiers.stream().map(Modifier::getKeyword).map(Modifier.Keyword::asString).collect(joining(" ")) + " ");
                }
            }

            private void printTypeParameters(final NodeList<TypeParameter> args, final Void arg) {
                if (!isNullOrEmpty(args)) {
                    printer.print("<");
                    for (final Iterator<TypeParameter> i = args.iterator(); i.hasNext(); ) {
                        final TypeParameter t = i.next();
                        t.accept(this, arg);
                        if (i.hasNext()) {
                            printer.print(", ");
                        }
                    }
                    printer.print(">");
                }
            }
        });
        System.out.println(compilationUnit.toString(prettyPrinterConfiguration));
//        FileUtils.write(file, compilationUnit.toString(), StandardCharsets.UTF_8);
    }
}
