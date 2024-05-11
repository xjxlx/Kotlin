package com.android.hcp3;

public class JarBean {
  /** 方法对应的具体的属性名字，例如：aC */
  private String attributeName;

  /** 方法的名字，例如：getAc */
  private String methodName;

  /** 泛型的路径，例如：de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject */
  private String genericPath;

  /** 泛型的名字，例如：SwitchControlObject */
  private String genericName;

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

  public String getGenericPath() {
    return genericPath;
  }

  public void setGenericPath(String genericPath) {
    this.genericPath = genericPath;
  }

  public String getGenericName() {
    return genericName;
  }

  public void setGenericName(String genericName) {
    this.genericName = genericName;
  }

  @Override
  public String toString() {
    return "JarBean{"
        + "attribute='"
        + attributeName
        + '\''
        + ", method='"
        + methodName
        + '\''
        + ", methodGenericityType='"
        + genericPath
        + '\''
        + ", objectName='"
        + genericName
        + '\''
        + '}';
  }
}
