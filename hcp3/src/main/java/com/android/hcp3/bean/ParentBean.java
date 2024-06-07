package com.android.hcp3.bean;

public class ParentBean {
  private String parentPath;
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
