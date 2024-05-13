package com.android.hcp3;

public class ApiNodeBean {
  /** 父类节点下，api方法的名字，例如：keys */
  private String apiName;

  private String apiReturnTypePath;

  /** 父类节点下，api方法返回类型泛型类型的全路径包名 */
  private String apiGenericPath;

  /** 父类节点下api返回类型泛型类型的名字 */
  private String apiGenericName;

  public String getApiName() {
    return apiName;
  }

  public void setApiName(String apiName) {
    this.apiName = apiName;
  }

  public String getApiReturnTypePath() {
    return apiReturnTypePath;
  }

  public void setApiReturnTypePath(String apiReturnTypePath) {
    this.apiReturnTypePath = apiReturnTypePath;
  }

  public String getApiGenericName() {
    return apiGenericName;
  }

  public void setApiGenericName(String apiGenericName) {
    this.apiGenericName = apiGenericName;
  }

  public String getApiGenericPath() {
    return apiGenericPath;
  }

  public void setApiGenericPath(String apiGenericPath) {
    this.apiGenericPath = apiGenericPath;
  }

  @Override
  public String toString() {
    return "ApiNodeBean{"
        + "apiName='"
        + apiName
        + '\''
        + ", apiPath='"
        + apiReturnTypePath
        + '\''
        + ", apiGenericName='"
        + apiGenericName
        + '\''
        + ", apiGenericPath='"
        + apiGenericPath
        + '\''
        + '}';
  }
}
