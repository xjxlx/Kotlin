package com.android.hcp3.bean;

public class ParentBean {
  /** 本地类所在的是哪个文件的地址，例如：technology.cariad.vehiclecontrolmanager.rsi.hvac.switchcontrols */
  private String parentPath;

  /** 本地类所在的是哪个类的名字，例如：SwitchControlEntity */
  private String parentEntityName;

  public String getParentPath() {
    return parentPath;
  }

  public void setParentPath(String parentPath) {
    this.parentPath = parentPath;
  }

  public String getParentEntityName() {
    return parentEntityName;
  }

  public void setParentEntityName(String parentEntityName) {
    this.parentEntityName = parentEntityName;
  }

  @Override
  public String toString() {
    return "ParentBean{"
        + "parentPath='"
        + parentPath
        + '\''
        + ", parentEntityName='"
        + parentEntityName
        + '\''
        + '}';
  }
}
