package com.xjx.kotlin.utils.hcp3;

import java.util.HashMap;
import java.util.Map;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;

public class WriteSDK {
  private static final String LIBS_FOLDER_PATH = "src/main/java/com/xjx/kotlin/utils/hcp3/jar/";
  private static final String TARGET_JAR_PATH = LIBS_FOLDER_PATH + "mib_rsi_android.jar";
  private static final String ENTITY_SUPERCLASS_PATH = "com.xjx.kotlin.utils.hcp3.BaseRSIValue";

  /**
   * @param targetFolderPath 文件写入成功后存放的位置，例如："./src/test"，注意，此处路径是以当前mode作为父节点的
   * @param argumentsMap 需要写入的参数集合
   * @param JarObjectPath
   *     JAR中需要写入的类，注意，此处需要全路径，例如："de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject"
   */
  public static void writeEntity(
      String targetFolderPath, HashMap<String, String> argumentsMap, String JarObjectPath) {
    try {
      ClassPool pool = ClassPool.getDefault();
      // 1：添加jar文件到类路径中，避免找不到类报错
      pool.appendClassPath(TARGET_JAR_PATH);
      // 2：构建一个类对象
      String objectName = JarObjectPath.substring(JarObjectPath.lastIndexOf(".") + 1);
      CtClass cls = pool.makeClass(objectName + "Entity");
      // 3：循环写入成员变量
      for (Map.Entry<String, String> entry : argumentsMap.entrySet()) {
        // 3.1：获取变量的类型
        CtClass type = pool.get(entry.getValue());
        // 3.2：构建变量对象
        CtField field = new CtField(type, entry.getKey(), cls);
        // 3.3：设置字段的访问修饰符为 private final
        field.setModifiers(Modifier.PRIVATE | Modifier.FINAL);
        // 3.4：给字段添加注解@Nullable
        AnnotationsAttribute attr =
            new AnnotationsAttribute(
                cls.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);
        Annotation nullableAnnotation =
            new Annotation("javax.annotation.Nullable", cls.getClassFile().getConstPool());
        attr.setAnnotation(nullableAnnotation);
        field.getFieldInfo().addAttribute(attr);
        // 3.5：在类中添加这个字段
        cls.addField(field);
      }

      // 4：添加构造方法
      // 4。1 获取构造方法的参数独享
      CtClass[] paramTypes = {pool.get(JarObjectPath)};
      // 4.2：添加类的父类对象
      cls.setSuperclass(pool.get(ENTITY_SUPERCLASS_PATH));
      CtConstructor constructor = CtNewConstructor.make(paramTypes, null, cls);
      // 设置构造方法的修饰符为protected
      constructor.setModifiers(Modifier.PROTECTED);
      // 设置构造函数的函数体
      //      constructor.setBody("super($1);");
      // 添加构造函数到类中
      cls.addConstructor(constructor);

      // 9：保存这个对象到文件中
      cls.writeFile(targetFolderPath);
      System.out.println("Custom type field added successfully.");
      // 清理
      cls.detach();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("An error occurred: " + e.getMessage());
    }
  }
}
