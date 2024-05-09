package com.xjx.kotlin.utils.hcp3.jar;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class ReadJarFile {
  private final String basePath = "libs/cluster46/";
  private final List<String> jarPathArray = new ArrayList<>();
  private final String localFilePath = "technology.cariad.vehiclecontrolmanager.TestBean";
  private final String jarClassPath =
      "de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject";

  public static Class<?> readJar(List<String> jarArrayPath, String className) {
    try {
      URL[] urls = new URL[jarArrayPath.size()];
      for (int i = 0; i < jarArrayPath.size(); i++) {
        urls[i] = new File(jarArrayPath.get(i)).toURI().toURL();
      }
      // 创建URLClassLoader来加载依赖的JAR包
      URLClassLoader classLoader = new URLClassLoader(urls, null);
      // 加载需要的类
      classLoader.loadClass("de.esolutions.fw.rudi.core.locators.IResourceLocator");
      classLoader.loadClass("de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject");
      classLoader.loadClass("de.esolutions.fw.rudi.viwi.service.hvac.v3.ValueControlObject");
      classLoader.loadClass("de.esolutions.fw.rudi.viwi.service.hvac.v3.FlavourCartridgeObject");
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

  private static Set<String> getMemberVariables(Class<?> clazz) {
    Set<String> memberVariables = new HashSet<>();
    try {
      if (clazz != null) {
        for (Method method : clazz.getDeclaredMethods()) {
          String name = method.getName();
          Type genericReturnType = method.getGenericReturnType();
          String typeName = genericReturnType.getTypeName();

          System.out.println("反射获取到的方法名字：[" + name + "]  typeName: [" + typeName + " ]");
        }
      }
      System.out.println("反射获取到的属性：" + memberVariables);
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
      jarPathArray.add(basePath + "fw_rudi_android_stapi.jar");
      jarPathArray.add(basePath + "fw_comm_android_stapi.jar");
      jarPathArray.add(basePath + "fw_comm_android_support.jar");
      jarPathArray.add(basePath + "fw_rsi_android_stapi.jar");
      jarPathArray.add(basePath + "fw_rsi_rx2_android_support.jar");
      jarPathArray.add(basePath + "fw_rudi_android_support.jar");
      jarPathArray.add(basePath + "fw_util_android_stapi.jar");
      jarPathArray.add(basePath + "fw_util_android_support.jar");
      jarPathArray.add(basePath + "mib_rsi_android.jar");

      Class<?> jarClass = readJar(jarPathArray, jarClassPath);
      Class<?> targetClass = readLocalClass(localFilePath);
      Set<String> jarMemberVariables = getMemberVariables(jarClass);
      Set<String> targetMemberVariables = getMemberVariables(targetClass);

      boolean success =
          writeVariable(jarMemberVariables, targetMemberVariables, jarClass, targetClass);
      System.out.println("write data success:" + success);
    } catch (Exception e) {
      System.out.println("write data error!");
    }
  }
}
