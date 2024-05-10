package com.xjx.kotlin.utils.hcp3;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;

public class WriteSDK {
  private static final String BASE_PATH = "src/main/java/com/xjx/kotlin/utils/hcp3/jar/";
  private static final String TARGET_JAR_PATH = BASE_PATH + "mib_rsi_android.jar";

  public static void writeEntity(
      String localFilePath, String key, String runType, String objectName) {
    try {
      ClassPool pool = ClassPool.getDefault();
      // 1：添加jar文件到类路径
      pool.appendClassPath(TARGET_JAR_PATH);
      //  2：获取要修改的类
      CtClass cls = pool.get(localFilePath);
      // 3：设置变量的类型
      CtClass type = pool.get(runType);
      // 4：构建变量对象
      CtField field = new CtField(type, key, cls);
      // 5：设置字段的访问修饰符为 private final
      field.setModifiers(Modifier.PRIVATE | Modifier.FINAL);
      // 6：给字段添加注解@Nullable
      AnnotationsAttribute attr =
          new AnnotationsAttribute(
              cls.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);
      Annotation nullableAnnotation =
          new Annotation("javax.annotation.Nullable", cls.getClassFile().getConstPool());
      attr.setAnnotation(nullableAnnotation);
      field.getFieldInfo().addAttribute(attr);

      // 7：在类中添加这个字段
      cls.addField(field);
      // cc.writeFile("/Users/XJX/Wokrs/StudioWorks/MY/Kotlin/app/src/test/java/com/xjx/kotlin");

      // 8:写入构造方法

      // 创建一个参数类型为String的构造方法
      CtClass[] paramTypes = {pool.get(objectName)};
      // 添加父类
      cls.setSuperclass(pool.get(objectName));
      CtConstructor constructor = CtNewConstructor.make(paramTypes, null, cls);
      // 设置构造方法的修饰符为protected
      constructor.setModifiers(Modifier.PROTECTED);
      // 设置构造函数的函数体
      //      constructor.setBody("super($1);");
      // 添加构造函数到类中
      cls.addConstructor(constructor);

      // 9：保存这个对象到文件中
      cls.writeFile();
      System.out.println("ok");

      System.out.println("Custom type field added successfully.");
      // 清理
      cls.detach();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("An error occurred: " + e.getMessage());
    }
  }
}
