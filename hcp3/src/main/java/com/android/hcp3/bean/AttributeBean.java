package com.android.hcp3.bean;

import java.util.LinkedHashSet;

public class AttributeBean {
  /** 生成属性类的路径，这个是全路径，例如：com.android.hcp3.rsi.hvac */
  private String attributePackage;

  /** 1：生成属性类的名字，例如：VcSpecialIndicationValue 2：基础类型或者忽略类型的名字，例如：String */
  private String name;

  /** 当前类所在的父类集合 */
  private LinkedHashSet<String> parentSet = new LinkedHashSet<>();

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

  public LinkedHashSet<String> getParentSet() {
    return parentSet;
  }

  public void setParentSet(LinkedHashSet<String> parentSet) {
    this.parentSet = parentSet;
  }

  @Override
  public String toString() {
    return "AttributeBean{"
        + "attributePackage='"
        + attributePackage
        + '\''
        + ", name='"
        + name
        + '\''
        + ", parentSet="
        + parentSet
        + '}';
  }
}
