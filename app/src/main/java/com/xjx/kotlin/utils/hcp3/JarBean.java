package com.xjx.kotlin.utils.hcp3;

public class JarBean {
  private String attribute;
  private String method;
  private String methodType;

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
        + '}';
  }
}
