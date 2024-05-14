package com.android.hcp3.bean;

public class AttributeBean {
  /** 生成属性类的路径，这个是全路径 */
  private String attributePackage;

  /** 生成属性类的名字 */
  private String name;

  public String getAttributePackage() {
    return attributePackage;
  }

  public void setAttributePackage(String attributePackage) {
    this.attributePackage = attributePackage;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "AttributeTypeBean{"
        + "path='"
        + attributePackage
        + '\''
        + ", name='"
        + name
        + '\''
        + '}';
  }
}
