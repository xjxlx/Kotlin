package com.android.hcp3.bean;

public class ApiNodeBean {
  /** 父类节点下，api方法的名字，例如：keys */
  private String apiName;

  private String apiReturnTypePath;

  /** 父类节点下，api方法返回类型泛型类型的全路径包名 */
  private String apiGenericPath;

  /** 父类节点下api返回类型泛型类型的名字 */
  private String apiGenericName;

  /**
   * api节点类中更新方法参数的名字 例如：
   * Api的类:de.esolutions.fw.rudi.viwi.service.hvacvehiclepreconditioning.v101.SwitchControlsApi
   * 其中的更新方法为:Single<URI> updateSwitchControlsObject(UUID var1, UpdateSwitchControlsObject var2)
   * 参数为：UpdateSwitchControlsObject，
   * 所以返回的名字为：de.esolutions.fw.rudi.viwi.service.hvacvehiclepreconditioning.v101
   */
  private String updatePackage;

  /**
   * api节点类中更新方法参数的名字 例如：
   * Api的类:de.esolutions.fw.rudi.viwi.service.hvacvehiclepreconditioning.v101.SwitchControlsApi
   * 其中的更新方法为:Single<URI> updateSwitchControlsObject(UUID var1, UpdateSwitchControlsObject var2)
   * 参数为：UpdateSwitchControlsObject， 所以返回的名字为：UpdateSwitchControlsObject
   */
  private String updateName;

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

  public String getApiGenericPath() {
    return apiGenericPath;
  }

  public void setApiGenericPath(String apiGenericPath) {
    this.apiGenericPath = apiGenericPath;
  }

  public String getApiGenericName() {
    return apiGenericName;
  }

  public void setApiGenericName(String apiGenericName) {
    this.apiGenericName = apiGenericName;
  }

  public String getUpdatePackage() {
    return updatePackage;
  }

  public void setUpdatePackage(String updatePackage) {
    this.updatePackage = updatePackage;
  }

  public String getUpdateName() {
    return updateName;
  }

  public void setUpdateName(String updateName) {
    this.updateName = updateName;
  }

  @Override
  public String toString() {
    return "ApiNodeBean{"
        + "apiName='"
        + apiName
        + '\''
        + ", apiReturnTypePath='"
        + apiReturnTypePath
        + '\''
        + ", apiGenericPath='"
        + apiGenericPath
        + '\''
        + ", apiGenericName='"
        + apiGenericName
        + '\''
        + ", updatePackage='"
        + updatePackage
        + '\''
        + ", updateName='"
        + updateName
        + '\''
        + '}';
  }
}
