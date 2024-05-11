package com.android.hcp3;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.File;
import java.io.IOException;
import javax.lang.model.element.Modifier;

public class Test {
  /** 生成代码存储的主路径 */
  private static String RSI_PROJECT_PATH = "hcp3/src/main/java/";

  private static String RSI_PROJECT_PACKAGE_PATH = "com/android/hcp3/generate/";

  /** rsi中大项的节点路径 */
  private static String RSI_PARENT_NODE_PATH = "hvac/v3/";

  /** rsi中大项中子节点路径 */
  private static String RSI_CHILD_NODE_PATH = "generalsettings";

  public static void main(String[] args) {
    // 定义属性
    FieldSpec field =
        FieldSpec.builder(String.class, "message").addModifiers(Modifier.PRIVATE).build();

    // 定义类
    TypeSpec helloWorld =
        TypeSpec.classBuilder("HelloWorld").addModifiers(Modifier.PUBLIC).addField(field).build();

    // 生成代码
    //     com.android.hcp3.generate.hvac.v3.generalsettings;
    String packagePath =
        (RSI_PROJECT_PACKAGE_PATH + RSI_PARENT_NODE_PATH + RSI_CHILD_NODE_PATH).replace("/", ".");
    JavaFile javaFile = JavaFile.builder(packagePath, helloWorld).build();

    // 输出到控制台
    try {
      File file = new File(RSI_PROJECT_PATH);
      //      javaFile.writeTo(System.out);
      javaFile.writeTo(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
