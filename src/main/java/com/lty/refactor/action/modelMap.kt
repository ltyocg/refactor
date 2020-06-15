package com.lty.refactor.action

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.symbolsolver.JavaSymbolSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver
import com.github.javaparser.utils.SourceRoot
import java.io.File

fun main() {
    val combinedTypeSolver = CombinedTypeSolver(
            JarTypeSolver("D:\\Program Files\\apache-maven-3.6.3\\repository\\in-project\\jpower\\3.7\\jpower-3.7.jar"),
            JavaParserTypeSolver("E:\\workplace\\maven\\Inspur.Dzzw.CloudAccept_V3.5\\src\\main\\java"),
            ReflectionTypeSolver()
    )
    val sourceRoot = SourceRoot(File("E:\\workplace\\maven\\Inspur.Dzzw.CloudAccept_V3.5\\src\\main\\java").toPath(), StaticJavaParser.getConfiguration().apply {
        setSymbolResolver(JavaSymbolSolver(combinedTypeSolver))
        isLexicalPreservationEnabled = true
    })
    sourceRoot.tryToParse("com.inspur.accept.systemsetting.notice.action", "AcceptNoticeController.java").result.get().findAll(ClassOrInterfaceDeclaration::class.java).forEach { classOrInterfaceDeclaration ->
        val resolve = classOrInterfaceDeclaration.extendedTypes[0].resolve()
        println(resolve.qualifiedName)
        val typeDeclaration = resolve.typeDeclaration.get()
    }
}