package com.xjx.kotlin.utils.hcp3;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javassist.*;

public class ReadJarFile {

  private static final List<String> jarPathArray = new ArrayList<>();
  private static final String BASE_PATH = "src/main/java/com/xjx/kotlin/utils/hcp3/jar/";
  private static final String TARGET_JAR_PATH = BASE_PATH + "mib_rsi_android.jar";

  private static final String TARGET_NODE_PATH = "de/esolutions/fw/rudi/viwi/service/hvac/v3";
  private static final String JAR_PATH =
      "de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject";
  private static final String LOCAL_PATH = "com.xjx.kotlin.utils.hcp3.Bean";

  /**
   * @return 1：返回指定JAR包中，指定targetPath 目录下所有object和Enum的集合的名字
   */
  private static List<String> readObjectClassName() {
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
   * @param className 指定的class类名
   * @param listObject 指定节点下所有的Object或者Enum的对象集合
   * @return 2：返回指定jar包中目标class类型的Class对象
   */
  public static Class<?> readJar(String className, List<String> listObject) {
    jarPathArray.add(BASE_PATH + "fw_comm_android_stapi.jar");
    jarPathArray.add(BASE_PATH + "fw_comm_android_support.jar");
    jarPathArray.add(BASE_PATH + "fw_rsi_android_stapi.jar");
    jarPathArray.add(BASE_PATH + "fw_rsi_rx2_android_support.jar");
    jarPathArray.add(BASE_PATH + "fw_rudi_android_stapi.jar");
    jarPathArray.add(BASE_PATH + "fw_rudi_android_support.jar");
    jarPathArray.add(BASE_PATH + "fw_util_android_stapi.jar");
    jarPathArray.add(BASE_PATH + "fw_util_android_support.jar");
    jarPathArray.add(TARGET_JAR_PATH);

    try {
      URL[] urls = new URL[jarPathArray.size()];
      for (int i = 0; i < jarPathArray.size(); i++) {
        urls[i] = new File(jarPathArray.get(i)).toURI().toURL();
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
      for (String s : listObject) {
        classLoader.loadClass(s);
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

  /**
   * @return 3：读取本地的文件，获取class类型文件
   */
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

  /**
   * @return 4：返回指定class的方法以及方法的泛型
   */
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

  private static boolean writeVariable(
      Map<String, String> jarMethodMap, Map<String, String> targetMethodMap) {

    List<String> deleteList = new ArrayList<>();
    // 1:先判断是否本地文件中有的数据，Jar文件中没有数据
    for (Map.Entry<String, String> entry : targetMethodMap.entrySet()) {
      String key = entry.getKey();
      if (!jarMethodMap.containsKey(key)) {
        deleteList.add(key);
      }
    }
    // 2：删除本地数据 todo
    Map<String, String> addMap = new HashMap<>();
    // 3:匹配jar和本地的集合，如果jar的集合中有本地集合中没有的数据，则添加到本地临时集合中
    for (Map.Entry<String, String> entry : jarMethodMap.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (!targetMethodMap.containsKey(key)) {
        addMap.put(key, value);
      }
    }
    // 4:添加本地数据
    try {
      for (Map.Entry<String, String> entry : addMap.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        write(key, value);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("write date error!");
    }
    return false;
  }

  public static void write(String key, String type) {
    try {
      // 创建类池
      ClassPool classPool = ClassPool.getDefault();
      // 设置类路径，指定要修改的类所在的路径
      classPool.insertClassPath("com.xjx.kotlin.utils.hcp3");
      // 加载类
      CtClass ctClass = classPool.get("com.xjx.kotlin.utils.hcp3.Bean");
      // 创建新属性，并指定类型
      CtField newField = new CtField(classPool.get(type), key, ctClass);
      // 添加新的属性
      ctClass.addField(newField);
      // 保存修改后的类文件
      ctClass.writeFile("com.xjx.kotlin.utils.hcp3");
      System.out.println("Field added successfully.");
    } catch (NotFoundException | CannotCompileException | IOException e) {
      e.printStackTrace();
      System.err.println("An error occurred: " + e.getMessage());
    }
  }

  public void execute() {
    try {

      // 1：读取指定目标节点下所有的object集合
      List<String> objectList = readObjectClassName();

      // 2：读取Jar包中指定的class类
      Class<?> jarClass = readJar(JAR_PATH, objectList);

      Class<?> targetClass = readLocalClass(LOCAL_PATH);
      Map<String, String> jarMethods = getMethods(jarClass);
      Map<String, String> targetMethods = getMethods(targetClass);

      boolean success = writeVariable(jarMethods, targetMethods);
      System.out.println("write data success:" + success);
    } catch (Exception e) {
      System.out.println("write data error!");
    }
  }
}
