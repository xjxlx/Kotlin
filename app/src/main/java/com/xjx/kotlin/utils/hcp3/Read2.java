package com.xjx.kotlin.utils.hcp3;

import java.util.ArrayList;
import java.util.List;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class Read2 {
  private static final String BASE_PATH = "src/main/java/com/xjx/kotlin/utils/hcp3/jar/";
  private static final String TARGET_JAR_PATH = BASE_PATH + "mib_rsi_android.jar";
  private static final String JAR_PATH =
      "de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject";

  public void read() throws Exception {
    List<String> list = new ArrayList<>();
    // 创建一个 ClassPool 对象
    ClassPool pool = ClassPool.getDefault();
    // 添加 JAR 包的路径，这里假设 JAR 文件位于当前目录下的 lib 目录中
    pool.insertClassPath(TARGET_JAR_PATH);
    // 获取类
    CtClass ctClass = pool.get(JAR_PATH);
    // 获取类的所有方法
    CtMethod[] methods = ctClass.getDeclaredMethods();

    // 打印方法名和返回值类型
    for (CtMethod method : methods) {
      // 获取方法名
      String methodName = method.getName();
      // 获取方法的返回值类型
      String returnType = method.getReturnType().getName();

      Object[] annotations = method.getAnnotations();
      if (annotations.length > 0) {
        System.out.println("annotations:size " + annotations.length);
      }

      if (methodName.startsWith("get")) {
        list.add(methodName);
        // 打印方法名和返回值类型

        // 获取方法签名
        System.out.println("Method: " + methodName + ", Return Type: ");
      }
    }
    System.out.println("Method:size " + list.size());

    // 关闭 ClassPool
    //    pool.close();
  }
}
