package com.android.hcp3.bean;

public class EnumBean {
  /** 枚举类的名字，例如：VcRestrictionReason */
  private String name;

  /** 枚举的路径，例如：hcp3/src/main/java/com/android/hcp3/rsi/hvac/VcRestrictionReason.java */
  private String path;

  /** 枚举在api文件夹中出现的次数 */
  private int count;

  /** 父类的节点路径，例如：com/android/hcp3/rsi/hvac/airoutlets */
  private String parentPath;

  /**
   * Api节点中Object的path，例如：hcp3/src/main/java/com/android/hcp3/rsi/hvac/generalsettings/GeneralSettingObjectEntity.java
   */
  private String apiChildPath;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getParentPath() {
    return parentPath;
  }

  public void setParentPath(String parentPath) {
    this.parentPath = parentPath;
  }

  public String getApiChildPath() {
    return apiChildPath;
  }

  public void setApiChildPath(String apiChildPath) {
    this.apiChildPath = apiChildPath;
  }

  @Override
  public String toString() {
    return "EnumBean{"
        + "name='"
        + name
        + '\''
        + ", path='"
        + path
        + '\''
        + ", count="
        + count
        + ", parentPath='"
        + parentPath
        + '\''
        + ", apiPath='"
        + apiChildPath
        + '\''
        + '}';
  }
}
