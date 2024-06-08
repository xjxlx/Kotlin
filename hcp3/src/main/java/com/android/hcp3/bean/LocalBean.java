package com.android.hcp3.bean;

import java.util.LinkedHashSet;

public class LocalBean implements Cloneable {
  /** 本地类的父目录的包名地址，例如：com.android.hcp3.rsi.hvac */
  private String localFileParentPackage = "";

  /** 本地类文件的名字，例如：VcSpecialIndicationValue */
  private String localFileName = "";

  /** jar中原始类的全路径地址，例如：de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchValue */
  private String jarOriginFilePath = "";

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
        + ", parentSet="
        + parentSet
        + '}';
  }
}
