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

  @Override
  public String toString() {
    return "ObjectBean{"
        + "attributeName='"
        + attributeName
        + '\''
        + ", methodName='"
        + methodName
        + '\''
        + ", genericPath='"
        + genericPackage
        + '\''
        + ", classType="
        + classType
        + '}';
  }
}
