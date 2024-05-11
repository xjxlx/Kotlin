package com.android.hcp3

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeSpec
import java.io.File
import java.io.IOException
import javax.lang.model.element.Modifier

object GenerateUtil {
    private const val LIBS_FOLDER_PATH: String = "src/main/java/com/xjx/kotlin/utils/hcp3/jar/"
    private const val TARGET_JAR_PATH: String = LIBS_FOLDER_PATH + "mib_rsi_android.jar"
    private const val ENTITY_SUPERCLASS_PATH: String = "com.xjx.kotlin.utils.hcp3.BaseRSIValue"

    /**
     *文件输出的路径
     */
    private val OUT_FOLDER = File("hcp3/src/main/java/com/android/hcp3")

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
//        val main =
//            MethodSpec.methodBuilder("main")
//                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//                .returns(Void.TYPE)
//                .addParameter(Array<String>::class.java, "args")
//                .addStatement("\$T.out.println(\$S)", System::class.java, "Hello, JavaPoet!")
//                .build()
//
//        val helloWorld =
//            TypeSpec.classBuilder("HelloWorld")
//                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                .addMethod(main)
//                .build()
//
//        val javaFile = JavaFile.builder("generate", helloWorld).build()
//        javaFile.writeTo(OUT_FOLDER)

        generateEntity()
    }

    fun generateEntity() {
        // 创建参数
        val parameter =
            ParameterSpec.builder(String::class.java, "android")
                .addModifiers(Modifier.FINAL)
                .build()

        // 构建构造方法的方法体内容
        val methodSpec = MethodSpec.constructorBuilder().addModifiers(Modifier.PROTECTED).addParameter(parameter).build()
        val helloWorld =
            TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(methodSpec)
                .build()

        val javaFile = JavaFile.builder("generate", helloWorld).build()
        javaFile.writeTo(OUT_FOLDER)
    }
}
