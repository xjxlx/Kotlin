package com.android.hcp3;

public class JarBean {
  private String attribute;
  private String method;
  private String methodType;
  private String methodGenericityType;
  private String objectName;

  public String getAttribute() {
    return attribute;
  }

  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getMethodType() {
    return methodType;
  }

  public void setMethodType(String methodType) {
    this.methodType = methodType;
  }

  public String getMethodGenericityType() {
    return methodGenericityType;
  }

  public void setMethodGenericityType(String methodGenericityType) {
    this.methodGenericityType = methodGenericityType;
  }

  public String getObjectName() {
    return objectName;
  }

  public void setObjectName(String objectName) {
    this.objectName = objectName;
  }

  @Override
  public String toString() {
    return "JarBean{"
        + "attribute='"
        + attribute
        + '\''
        + ", method='"
        + method
        + '\''
        + ", methodType='"
        + methodType
        + '\''
        + ", methodGenericityType='"
        + methodGenericityType
        + '\''
        + ", objectName='"
        + objectName
        + '\''
        + '}';
  }
}
