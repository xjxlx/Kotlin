package com.android.hcp3;

import java.util.Iterator;
import java.util.LinkedHashSet;

// import javassist.*;
// import javassist.bytecode.AnnotationsAttribute;
// import javassist.bytecode.annotation.Annotation;

public class WriteSDK {
  private static final String LIBS_FOLDER_PATH = "src/main/java/com/xjx/kotlin/utils/hcp3/jar/";
  private static final String TARGET_JAR_PATH = LIBS_FOLDER_PATH + "mib_rsi_android.jar";
  private static final String ENTITY_SUPERCLASS_PATH = "com.xjx.kotlin.utils.hcp3.BaseRSIValue";

  /**
   * @param targetFolderPath 文件写入成功后存放的位置，例如："./src/test"，注意，此处路径是以当前mode作为父节点的
   * @param beanSet 需要写入的参数集合
   * @param JarObjectPath
   *     JAR中需要写入的类，注意，此处需要全路径，例如："de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject"
   */
  public static void writeEntity(
      String targetFolderPath, LinkedHashSet<JarBean> beanSet, String JarObjectPath) {
    try {
      //      ClassPool pool = ClassPool.getDefault();
      //      // 1：添加jar文件到类路径中，避免找不到类报错
      //      pool.appendClassPath(TARGET_JAR_PATH);
      //
      //      // 2：构建一个类对象
      //      String objectName = JarObjectPath.substring(JarObjectPath.lastIndexOf(".") + 1);
      //      CtClass cls = pool.makeClass(objectName + "Entity");

      // 3：循环写入成员变量
      StringBuilder bodyBuffer = new StringBuilder();
      bodyBuffer.append("{\n").append("super($1);");
      Iterator<JarBean> iterator = beanSet.iterator();
      JarBean bean = iterator.next();

      // "java.util.Optional<de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject>");
      //      CtClass type = pool.get("com.xjx.kotlin.utils.hcp3.HvacSwitchControlEntity");
      //
      //      // 3.2：构建变量对象
      //      CtField field = new CtField(type, bean.getAttribute(), cls);
      //      // 3.3：设置字段的访问修饰符为 private final
      //      field.setModifiers(Modifier.PRIVATE | Modifier.FINAL);
      //      // 3.4：给字段添加注解@Nullable
      //      AnnotationsAttribute attr =
      //          new AnnotationsAttribute(
      //              cls.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);
      //      Annotation nullableAnnotation =
      //          new Annotation("javax.annotation.Nullable", cls.getClassFile().getConstPool());
      //      attr.setAnnotation(nullableAnnotation);
      //      field.getFieldInfo().addAttribute(attr);
      //      // 3.5：在类中添加这个字段
      //      cls.addField(field);

      //      bodyBuffer
      //          .append("this.")
      //          .append("aC")
      //          .append(" = $1.")
      //          .append("getAC")
      //          .append("()")
      //          //
      //          //
      // .append(".map(de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject::new)")
      //          .append(".map(")
      //          .append(
      //              "new Function<SwitchControlObject, HvacSwitchControlEntity>() {\n"
      //                  + "@Override\n"
      //                  + "                      public HvacSwitchControlEntity
      // apply(SwitchControlObject\n"
      //                  + "       switchControlObject) {\n"
      //                  + "                        return new
      // HvacSwitchControlEntity(switchControlObject);\n"
      //                  + "                      }\n"
      //                  + "                    }) ")
      //          .append(")")
      //          .append(".orElse(null);")
      //          .append(";");

      // 创建一个新的方法
      String body2 =
          " object"
              + "              .getAC().map(new Function/*<SwitchControlObject, HvacSwitchControlEntity>*/() {"
              + "                  @Override"
              + "                  public HvacSwitchControlEntity apply(SwitchControlObject switchControlObject) {"
              + "                      return new HvacSwitchControlEntity(switchControlObject);"
              + "                  }"
              + "              }).orElse(null);";
      System.out.println("body2");
      String body3 = "System.out.println(\"body2\");";
      //
      //
      //      // 4：添加构造方法
      //      // 4.1 获取构造方法的参数独享
      //      CtClass[] paramTypes = {pool.get(JarObjectPath)};
      //      // 4.2：添加类的父类对象
      //      cls.setSuperclass(pool.get(ENTITY_SUPERCLASS_PATH));
      //      CtConstructor constructor = CtNewConstructor.make(paramTypes, null, cls);
      //      // 设置构造方法的修饰符为protected
      //      constructor.setModifiers(Modifier.PROTECTED);
      //      // 设置body
      //      bodyBuffer.append("}");
      //      String body = bodyBuffer.toString();
      //      System.out.println("body:" + body);
      //      constructor.setBody(body);
      //      cls.addConstructor(constructor);
      //      // 9：保存这个对象到文件中
      //      cls.writeFile(targetFolderPath);
      System.out.println("Custom type field added successfully.");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("An error occurred: " + e.getMessage());
    }
  }
}
