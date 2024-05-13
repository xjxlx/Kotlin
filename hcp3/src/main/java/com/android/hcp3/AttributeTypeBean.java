package com.android.hcp3;

public class AttributeTypeBean {
  /** 生成属性类的路径，这个是全路径 */
  private String path;

  /** 生成属性类的名字 */
  private String name;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "AttributeTypeBean{" + "path='" + path + '\'' + ", name='" + name + '\'' + '}';
  }
}
