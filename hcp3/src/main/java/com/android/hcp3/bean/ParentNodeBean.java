package com.android.hcp3.bean;

public class ParentNodeBean {
  /**
   * api中master的文件名字，例如：HVACVehiclePreconditioning,这个是
   * {com.android.hcp3.Config.RSI_PARENT_NODE_PATH}对应的根路径的主类
   */
  private String parentNodeName;

  /** api中master的包名，例如：de.esolutions.fw.rudi.viwi.service.hvacvehiclepreconditioning.v100 */
  private String parentNodePackage;

  public String getParentNodeName() {
    return parentNodeName;
  }

  public void setParentNodeName(String parentNodeName) {
    this.parentNodeName = parentNodeName;
  }

  public String getParentNodePackage() {
    return parentNodePackage;
  }

  public void setParentNodePackage(String parentNodePackage) {
    this.parentNodePackage = parentNodePackage;
  }

  @Override
  public String toString() {
    return "ParentNodeBean{"
        + "parentNodeName='"
        + parentNodeName
        + '\''
        + ", parentNodePackage='"
        + parentNodePackage
        + '\''
        + '}';
  }
}
