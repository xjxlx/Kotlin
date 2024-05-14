package com.android.hcp3.bean;

import com.android.hcp3.ClassTypeEnum;

public class ObjectBean {
  /** 方法对应的具体的属性名字，例如：aC */
  private String attributeName;

  /** 方法的名字，例如：getAc */
  private String methodName;

  /** 泛型的路径，例如：de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject */
  private String genericPackage;

  /** 判断当前的class是什么类型 */
  private ClassTypeEnum classType = ClassTypeEnum.INVALID;

  /** 忽略的本地类的package */
  private String ignorePackage;

  public ObjectBean() {}

  public ObjectBean(String genericPackage, String ignorePackage) {
    this.genericPackage = genericPackage;
    this.ignorePackage = ignorePackage;
  }

  public String getAttributeName() {
    return attributeName;
  }

  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getGenericPackage() {
    return genericPackage;
  }

  public void setGenericPackage(String genericPackage) {
    this.genericPackage = genericPackage;
  }

  public ClassTypeEnum getClassType() {
    return classType;
  }

  public void setClassType(ClassTypeEnum classType) {
    this.classType = classType;
  }

  public String getIgnorePackage() {
    return ignorePackage;
  }

  public void setIgnorePackage(String ignorePackage) {
    this.ignorePackage = ignorePackage;
  }

  @Override
  public String toString() {
    return "ObjectBean{"
        + "attributeName='"
        + attributeName
        + '\''
        + ", methodName='"
        + methodName
        + '\''
        + ", genericPackage='"
        + genericPackage
        + '\''
        + ", classType="
        + classType
        + ", ignorePackage='"
        + ignorePackage
        + '\''
        + '}';
  }
}
