package com.android.hcp3;

public class ObjectEntity {
  /** 方法对应的具体的属性名字，例如：aC */
  private String attributeName;

  /** 方法的名字，例如：getAc */
  private String methodName;

  /** 泛型的路径，例如：de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject */
  private String genericPath;

  /**
   * 判断当前的class是什么类型 0：默认无效的数据类型，1：基础数据类型，例如，Float、Boolean、Integer 2：数组类型，
   * 3：List数据集合，4：其他数据类型，也就是自定义的数据类型
   */
  private int classType;

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

  public int getClassType() {
    return classType;
  }

  /**
   * @param classType 0：默认无效的数据类型，1：基础数据类型，例如，Float、Boolean、Integer
   *     2：数组类型，3：List数据集合，4：其他数据类型，也就是自定义的数据类型
   */
  public void setClassType(int classType) {
    this.classType = classType;
  }

  @Override
  public String toString() {
    return "ObjectEntity{"
        + "attributeName='"
        + attributeName
        + '\''
        + ", methodName='"
        + methodName
        + '\''
        + ", genericPath='"
        + genericPath
        + '\''
        + ", classType="
        + classType
        + '}';
  }
}
