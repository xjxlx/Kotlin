package com.xjx.kotlin.utils.hcp3;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReadJarFile {

  private static final List<String> jarPathArray = new ArrayList<>();
  private static final String BASE_PATH = "src/main/java/com/xjx/kotlin/utils/hcp3/jar/";
  private static final String LOCAL_PATH = "technology.cariad.vehiclecontrolmanager.TestBean";
  private static final String JAR_PATH =
      "de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject";
  private static final String JAR_PATH2 =
      "de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObjectImpl";

  private static final String TARGET_JAR_PATH = BASE_PATH + "mib_rsi_android.jar";
  private static final String TARGET_NODE_PATH = "de/esolutions/fw/rudi/viwi/service/hvac/v3";

  /**
   * @return 返回指定JAR包中，指定targetPath 目录下所有object的集合
   */
  public static List<String> readObjectClassName() {
    List<String> fileNames = new ArrayList<>();
    try {
      // 打开Jar文件
      JarFile jarFile = new JarFile(TARGET_JAR_PATH);
      // 获取Jar包中的所有文件和目录条目
      Enumeration<JarEntry> entries = jarFile.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        String entryName = entry.getName();
        // 收集指定路径下的所有文件名称
        if (entryName.startsWith(TARGET_NODE_PATH)) {
          if (entryName.contains(".class")) {
            String splitClassName = entryName.split(".class")[0];
            if (((splitClassName.endsWith("Object")) || (splitClassName.endsWith("Enum")))
                && (splitClassName.contains("/"))) {
              String replaceName = splitClassName.replace("/", ".");
              fileNames.add(replaceName);
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("读取指定列表中Object列表失败：" + e.getMessage());
    }
    return fileNames;
  }

  /**
   * @param jarArrayPath JAR包的集合，里面存着所有引用的JAR包
   * @param className 指定的class类名
   * @param listObject 指定节点下所有的Object或者Enum的对象集合
   * @return 返回指定class类型的Class对象
   */
  public static Class<?> readJar(
      List<String> jarArrayPath, String className, List<String> listObject) {
    try {
      URL[] urls = new URL[jarArrayPath.size()];
      for (int i = 0; i < jarArrayPath.size(); i++) {
        urls[i] = new File(jarArrayPath.get(i)).toURI().toURL();
      }
      // 创建URLClassLoader来加载依赖的JAR包
      URLClassLoader classLoader = new URLClassLoader(urls, null);
      // 加载需要的类
      classLoader.loadClass("de.esolutions.fw.rudi.core.locators.IResourceLocator");
      classLoader.loadClass("de.esolutions.fw.rudi.core.IPath");

      classLoader.loadClass("de.esolutions.fw.util.commons.genericdata.FwElement");
      classLoader.loadClass("de.esolutions.fw.util.commons.genericdata.FwPrimitive");
      classLoader.loadClass("de.esolutions.fw.util.commons.genericdata.FwObject");
      classLoader.loadClass("de.esolutions.fw.util.commons.genericdata.ObjectKey");
      for (int i = 0; i < listObject.size(); i++) {
        classLoader.loadClass(listObject.get(i));
      }

      Class<?> jarClass = classLoader.loadClass(className);
      // 关闭ClassLoader释放资源
      classLoader.close();
      System.out.println("顺利读取到了JAR包中的Class文件：" + jarClass);
      return jarClass;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("读取JAR包中的Class文件异常：" + e.getMessage());
      return null;
    }
  }

  public static Class<?> readLocalClass(String className) {
    try {
      Class<?> targetClass = Class.forName(className);
      System.out.println("顺利读取本地的Class文件成功：" + targetClass);
      return targetClass;
    } catch (Exception e) {
      System.out.println("读取本地的Class文件异常：" + e.getMessage());
      return null;
    }
  }

  private static Map<String, String> getMethods(Class<?> clazz) {
    Map<String, String> methodMap = new HashMap<>();
    try {
      if (clazz != null) {
        // 将数组转换为集合
        Set<Method> methods = new HashSet<>(Arrays.asList(clazz.getDeclaredMethods()));

        for (Method method : methods) {
          String methodName = method.getName();
          String resultMethodName = "";
          String resultMethodRunType = "";
          // 1： 必须是以get开头的方法
          if (methodName.startsWith("get")) {
            // 2：过滤掉桥接方法和合成方法
            if (!method.isBridge() && !method.isSynthetic()) {
              // 3:去掉get并转换首字母为小写
              String splitGetName = methodName.split("get")[1];
              resultMethodName =
                  splitGetName.substring(0, 1).toLowerCase() + splitGetName.substring(1);

              // 4:获取方法的返回类型
              Type returnType = method.getGenericReturnType();
              if (returnType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) returnType;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0) {
                  resultMethodRunType = actualTypeArguments[0].getTypeName();
                }
              }
              System.out.println(
                  "method [" + resultMethodName + "] type:[" + resultMethodRunType + "]");
              methodMap.put(resultMethodName, "");
            }
          }
        }
      }
      System.out.println("反射获取到的属性：" + methodMap.size());
    } catch (Exception e) {
      System.out.println("反射属性异常：" + e.getMessage());
    }

    return methodMap;
  }

  private static Set<String> getMemberVariables(Class<?> clazz) {
    Set<String> memberVariables = new HashSet<>();
    try {
      if (clazz != null) {
        // 将数组转换为集合
        Set<Field> Fields = new HashSet<>(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : Fields) {
          field.setAccessible(true);
          String type = field.getGenericType().getTypeName();

          String methodName = field.getName();
          if (methodName.startsWith("KEY_")) {
            String claN = field.getDeclaringClass().getName();
            System.out.println("反射获取到的属性：" + methodName + " --->" + type);
            memberVariables.add(methodName);
          }
        }
      }
      System.out.println("反射获取到的属性：" + memberVariables.size());
    } catch (Exception e) {
      System.out.println("反射属性异常：" + e.getMessage());
    }

    return memberVariables;
  }

  private static boolean writeVariable(
      Set<String> jarVariable,
      Set<String> targetVariable,
      Class<?> jarClass,
      Class<?> targetClass) {
    try {
      if (jarVariable.size() > targetVariable.size()) {
        Set<String> extraVariables = new HashSet<>(jarVariable);
        extraVariables.removeAll(targetVariable);
        // 将多余的成员变量复制到文件 B 中去
        for (String variable : extraVariables) {
          Field field = jarClass.getDeclaredField(variable);
          field.setAccessible(true);
          Object value = field.get(null); // 静态变量，传入 null
          Field fieldB = targetClass.getDeclaredField(variable);
          fieldB.setAccessible(true);
          fieldB.set(null, value); // 静态变量，传入 null
          System.out.println("Copied variable " + variable + " from class A to class B");
        }
      } else {
        System.out.println("No extra member variables found in class jar");
      }
    } catch (Exception e) {
      System.out.println("write date error!");
    }
    return false;
  }

  public void execute() {
    try {
      jarPathArray.add(BASE_PATH + "fw_comm_android_stapi.jar");
      jarPathArray.add(BASE_PATH + "fw_comm_android_support.jar");
      jarPathArray.add(BASE_PATH + "fw_rsi_android_stapi.jar");
      jarPathArray.add(BASE_PATH + "fw_rsi_rx2_android_support.jar");
      jarPathArray.add(BASE_PATH + "fw_rudi_android_stapi.jar");
      jarPathArray.add(BASE_PATH + "fw_rudi_android_support.jar");
      jarPathArray.add(BASE_PATH + "fw_util_android_stapi.jar");
      jarPathArray.add(BASE_PATH + "fw_util_android_support.jar");
      jarPathArray.add(TARGET_JAR_PATH);

      // 1：读取指定目标节点下所有的object集合
      List<String> objectList = readObjectClassName();

      // 2：读取
      Class<?> jarClass = readJar(jarPathArray, JAR_PATH, objectList);

      Class<?> targetClass = readLocalClass(LOCAL_PATH);
      Map<String, String> methods = getMethods(jarClass);
      //      Set<String> targetMemberVariables = getMemberVariables(jarClass);

      //      boolean success =
      //          writeVariable(jarMemberVariables, targetMemberVariables, jarClass, targetClass);
      //      System.out.println("write data success:" + success);
    } catch (Exception e) {
      System.out.println("write data error!");
    }
  }
}
