package com.android.hcp3;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.File;
import java.io.IOException;
import javax.lang.model.element.Modifier;

public class DT {
  public static void main(String[] args) throws IOException {
    MethodSpec main =
        MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addParameter(String[].class, "args")
            .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
            .build();

    TypeSpec helloWorld =
        TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(main)
            .build();

    JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld).build();
    File file = new File("com.android.hcp3");
    //    javaFile.writeTo(System.out);
    javaFile.writeTo(file);
  }
}
