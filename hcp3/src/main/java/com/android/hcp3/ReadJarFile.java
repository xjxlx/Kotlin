package com.android.hcp3;

import static com.android.hcp3.Config.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReadJarFile {

  private static final String LOCAL_PATH = "com.xjx.kotlin.utils.hcp3.Bean";
  private static URLClassLoader mClassLoader;

  public static void main(String[] args) {
    execute();
  }

  /**
   * @param filterNodePath
   *     过滤指定父类节点的包名，注意这里的节点类型不是包名，是反斜杠的路径，例如：de/esolutions/fw/rudi/viwi/service/hvac/v3
   * @return 1：返回指定JAR包中，指定targetPath 目录下所有object和Enum的集合的名字
   */
  private static List<String> readObjectClassName(String filterNodePath) {
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
        if (entryName.startsWith(filterNodePath)) {
          // System.out.println("entryName:" + entryName);
          String parentNodeName = StringUtil.capitalize(RSI_PARENT_NODE_PATH);
          if (entryName.contains(".class")) {
            String splitClassName = entryName.split(".class")[0];
            if (((splitClassName.endsWith("Object"))
                    || (splitClassName.endsWith("Enum"))
                    || (splitClassName.endsWith(parentNodeName)))
                && (splitClassName.contains("/"))) {

              // 如果是以父类节点结束的，则保存这个节点的全属性包名
              if (splitClassName.endsWith(parentNodeName)) {
                System.out.println("rsiTargetNodePath:" + splitClassName);
                rsiTargetNodePath = splitClassName;
              }
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
    ArrayList<String> jarList = new ArrayList<>();
    File folder = new File(BASE_JAR_PATH);
    // 加载指定位置的jar包到classLoad里面
    if (folder.exists() && folder.isDirectory()) {
      // 获取文件夹下的所有文件
      File[] files = folder.listFiles();
      if (files != null) {
        // 遍历文件数组，输出文件名
        for (File file : files) {
          // System.out.println("JAR-Name: " + file.getName());
          jarList.add(file.getName());
        }
      }
    } else {
      System.out.println("读取目标Jar文件夹中的JAR异常！");
    }

    try {
      URL[] urls = new URL[jarList.size()];
      for (int i = 0; i < jarList.size(); i++) {
        urls[i] = new File(BASE_JAR_PATH + jarList.get(i)).toURI().toURL();
      }

      // 创建URLClassLoader来加载依赖的JAR包
      if (mClassLoader == null) {
        mClassLoader = new URLClassLoader(urls, null);
        // 加载需要的类
        mClassLoader.loadClass("de.esolutions.fw.rudi.core.locators.IResourceLocator");
        mClassLoader.loadClass("de.esolutions.fw.rudi.core.IPath");

        mClassLoader.loadClass("de.esolutions.fw.util.commons.genericdata.FwElement");
        mClassLoader.loadClass("de.esolutions.fw.util.commons.genericdata.FwPrimitive");
        mClassLoader.loadClass("de.esolutions.fw.util.commons.genericdata.FwObject");
        mClassLoader.loadClass("de.esolutions.fw.util.commons.genericdata.ObjectKey");
        mClassLoader.loadClass("io.reactivex.rxjava3.core.Single");
        for (String classPath : listObject) {
          mClassLoader.loadClass(classPath);
        }
      }

      Class<?> jarClass = mClassLoader.loadClass(className);
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
  private static LinkedHashSet<ObjectEntity> getMethods(Class<?> clazz, String tag) {
    LinkedHashSet<ObjectEntity> set = new LinkedHashSet<>();
    try {
      if (clazz != null) {
        // 将数组转换为集合
        Set<Method> methods = new HashSet<>(Arrays.asList(clazz.getDeclaredMethods()));

        for (Method method : methods) {
          String methodName = method.getName();
          String attributeName = "";
          String genericPath = "";
          // 1： 必须是以get开头的方法
          if (methodName.startsWith("get")) {
            ObjectEntity bean = new ObjectEntity();

            // 2：过滤掉桥接方法和合成方法
            if (!method.isBridge() && !method.isSynthetic()) {
              // 3:去掉get并转换首字母为小写
              String splitGetName = methodName.split("get")[1];
              attributeName =
                  splitGetName.substring(0, 1).toLowerCase() + splitGetName.substring(1);

              // 4:获取方法的返回类型
              Type returnType = method.getGenericReturnType();
              // 5:返回泛型的类型
              if (returnType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) returnType;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0) {
                  Type argument = actualTypeArguments[0];
                  genericPath = argument.getTypeName();
                  bean.setClassType(checkClassType(argument));
                }
              }

              bean.setAttributeName(attributeName);
              bean.setMethodName(methodName);
              bean.setGenericPath(genericPath);

              System.out.println(
                  "      method [" + attributeName + "] GenericType:[" + genericPath + "]");
              set.add(bean);
            }
          }
        }
      }
      System.out.println("[" + tag + "]" + "反射获取到的属性：" + set.size());
    } catch (Exception e) {
      System.out.println("[" + tag + "]" + "反射属性异常：" + e.getMessage());
    }
    return set;
  }

  /**
   * @param jarMethodSet jar包中的变量集合
   * @param targetMethodSet 本地类中变量的集合
   * @return 检测是否需要写入属性
   */
  private static boolean checkNeedWriteVariable(
      LinkedHashSet<ObjectEntity> jarMethodSet, LinkedHashSet<ObjectEntity> targetMethodSet) {
    if (jarMethodSet.size() != targetMethodSet.size()) {
      System.out.println("本地数据和jar包数据不相同，需要重新写入数据！");
      return false;
    } else {
      System.out.println("本地数据和jar包数据相同，需要对比数据内容是否相同！");
      return jarMethodSet.equals(targetMethodSet);
    }
  }

  /**
   * @return 返回当前的class是什么数据类型 0：默认无效的数据类型，1：基础数据类型，例如，Float、Boolean、Integer *
   *     2：数组类型，3：List数据集合，4：自定义Object数据类型
   */
  private static int checkClassType(Type type) {
    int classType = 0;
    Class<?> typeClass = null;
    if (type instanceof ParameterizedType parameterizedType) { // 泛型类型
      Type rawType = parameterizedType.getRawType();
      if (rawType instanceof Class<?>) {
        typeClass = (Class<?>) rawType;
      }
    } else if (type instanceof Class<?> cls) { // 不是泛型类型的参数
      typeClass = cls;
    }

    if (typeClass != null) {
      if (isPrimitiveOrWrapper(typeClass)) { // 基本数据类型
        classType = 1;
        // System.out.println("基本数据类型");
      } else if (typeClass.isArray()) { // 数组类型
        classType = 2;
        // System.out.println("数组类型");
      } else if (List.class.isAssignableFrom(typeClass)) { //  List数据类型
        classType = 3;
        // System.out.println("List数据类型");
      } else { // 其他引用数据类型，也就是自定义的object数据类型
        classType = 4;
        // System.out.println("自定义Object数据类型");
      }
    }
    return classType;
  }

  /**
   * @param clazz class的对象
   * @return 判断是否是基本数据类型
   */
  private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
    return clazz.isPrimitive()
        || clazz == Integer.class
        || clazz == Double.class
        || clazz == Boolean.class
        || clazz == Character.class
        || clazz == Byte.class
        || clazz == Short.class
        || clazz == Long.class
        || clazz == Float.class;
  }

  private static void loadParentNode() {
    // 使用类加载器，读取父类中主节点的接口变量
    if (!rsiTargetNodePath.isEmpty()) {
      try {
        Class<?> parentNodeClass =
            mClassLoader.loadClass(StringUtil.transitionPath(rsiTargetNodePath));
        // 获取类的所有方法
        for (Method method : parentNodeClass.getDeclaredMethods()) {
          System.out.println(method.getName());
        }
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void execute() {
    try {
      // 1：读取指定目标节点下所有的object集合
      // de/esolutions/fw/rudi/viwi/service/hvac/v3
      String filterNodePath =
          Paths.get(RSI_ROOT_NODE_PATH)
              .resolve(Paths.get(RSI_PARENT_NODE_PATH))
              .resolve(Paths.get(RSI_PARENT_NODE_LEVEL))
              .toString()
              .replace(".", "/");
      System.out.println("过滤JAR包中的父节点为： " + filterNodePath);
      List<String> objectList = readObjectClassName(filterNodePath);
      // 2：读取Jar包中指定的class类
      Class<?> jarClass = readJar(RSI_CHILD_NODE_OBJECT_NAME, objectList);
      Class<?> targetClass = readLocalClass(LOCAL_PATH);
      LinkedHashSet<ObjectEntity> jarSet = getMethods(jarClass, "JAR");
      LinkedHashSet<ObjectEntity> localSet = getMethods(targetClass, "Local");

      boolean needWriteVariable = checkNeedWriteVariable(jarSet, localSet);
      if (needWriteVariable) {
        System.out.println("属性完全相同，不需要重新写入属性！");
      } else {
        loadParentNode();
        GenerateUtil.generateEntity(jarSet);
      }
      // 关闭ClassLoader释放资源
      mClassLoader.close();
      mClassLoader = null;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("write data error!");
    }
  }
}
