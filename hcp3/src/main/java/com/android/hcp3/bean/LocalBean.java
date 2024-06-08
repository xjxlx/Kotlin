package com.android.hcp3.bean;

import com.android.hcp3.ClassTypeEnum;
import java.util.LinkedHashSet;

public class LocalBean implements Cloneable {
  /** 本地类的父目录的包名地址，例如：com.android.hcp3.rsi.hvac */
  private String localFileParentPackage = "";

  /** 本地类文件的名字，例如：VcSpecialIndicationValue */
  private String localFileName = "";

  /**
   * 1：jar中原始类的全路径地址，例如：de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchValue
   * 2：需要注意的是，只有第一次生成的时候会有数据，在运行第二次的时候，子类的Entity下的类不会有任何的数据，因为子类的Entity已经生成了，就不会再去遍历了
   */
  private String jarOriginFilePath = "";

  /**
   * 1：当前类的类型，用于区分被挪移的死那哪种类，目前只需要挪移Enum的类就可以了
   * 2：需要注意的是，只有第一次生成的时候会有数据，在运行第二次的时候，子类的Entity下的类不会有任何的数据，因为子类的Entity已经生成了，就不会再去遍历了
   */
  private ClassTypeEnum classTypeEnum = ClassTypeEnum.INVALID;

  /** 当前类所在的父类集合 */
  private LinkedHashSet<ParentBean> parentSet = new LinkedHashSet<>();

  public String getLocalFileParentPackage() {
    return localFileParentPackage;
  }

  public void setLocalFileParentPackage(String localFileParentPackage) {
    this.localFileParentPackage = localFileParentPackage;
  }

  public String getLocalFileName() {
    return localFileName;
  }

  public void setLocalFileName(String localFileName) {
    this.localFileName = localFileName;
  }

  public String getJarOriginFilePath() {
    return jarOriginFilePath;
  }

  public void setJarOriginFilePath(String jarOriginFilePath) {
    this.jarOriginFilePath = jarOriginFilePath;
  }

  public ClassTypeEnum getClassTypeEnum() {
    return classTypeEnum;
  }

  public void setClassTypeEnum(ClassTypeEnum classTypeEnum) {
    this.classTypeEnum = classTypeEnum;
  }

  public LinkedHashSet<ParentBean> getParentSet() {
    return parentSet;
  }

  public void setParentSet(LinkedHashSet<ParentBean> parentSet) {
    this.parentSet = parentSet;
  }

  @Override
  public String toString() {
    return "LocalBean{"
        + "localFileParentPackage='"
        + localFileParentPackage
        + '\''
        + ", localFileName='"
        + localFileName
        + '\''
        + ", jarOriginFilePath='"
        + jarOriginFilePath
        + '\''
        + ", classTypeEnum="
        + classTypeEnum
        + ", parentSet="
        + parentSet
        + '}';
  }
}
