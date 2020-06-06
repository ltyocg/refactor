package com.lty.refactor.action

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.symbolsolver.JavaSymbolSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver
import java.io.File

fun main() {
    val jarTypeSolver = JarTypeSolver("D:\\Program Files\\apache-maven-3.6.3\\repository\\in-project\\jpower\\3.7\\jpower-3.7.jar")
    val javaSymbolSolver = JavaSymbolSolver(CombinedTypeSolver(jarTypeSolver, JavaParserTypeSolver("E:\\workplace\\maven\\Inspur.Dzzw.CloudAccept_V3.5\\src\\main\\java")))
    StaticJavaParser.getConfiguration().setSymbolResolver(javaSymbolSolver)
    val compilationUnit = StaticJavaParser.parse(File("E:\\workplace\\maven\\Inspur.Dzzw.CloudAccept_V3.5\\src\\main\\java\\com\\inspur\\accept\\monitor\\allbusiness\\action\\AllBusinessController.java"))
    compilationUnit.findAll(ClassOrInterfaceDeclaration::class.java).forEach {
        println(it.extendedTypes[0].resolve().qualifiedName)
    }
}