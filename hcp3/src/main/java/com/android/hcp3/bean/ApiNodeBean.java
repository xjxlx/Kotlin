package com.android.hcp3.bean;

import de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.HeadUpDisplay;

/**
 *
 *
 * <ol>
 *   节点是： {@link de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.HeadUpDisplay}
 *   <ol>
 *     里面包含了以下Api
 *     <li>ContentLocationsApi contentLocations();
 *     <li>HudFieldsApi hudFields();
 *     <li>HudSettingsApi hudSettings();
 *     <li>SwitchControlsApi switchControls();
 *     <li>ValueControlsApi valueControls();
 *     <li>ViewsApi views();
 *   </ol>
 * </ol>
 */
public class ApiNodeBean {
  /**
   * 节点下Api的名字，例如：
   *
   * <ul>
   *   <li>节点是：{@link de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.HeadUpDisplay}
   *   <li>return 节点的名字是：{@link HeadUpDisplay#switchControls()}
   * </ul>
   */
  private String apiName;

  /**
   * 节点下的Api路径，例如：
   *
   * <ul>
   *   <li>节点是：{@link de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.HeadUpDisplay}
   *   <li>节点的名字是：{@link HeadUpDisplay#switchControls()}
   *   <li>return 节点下的Api路径是：{@link
   *       de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.SwitchControlsApi}
   * </ul>
   */
  private String apiPath;

  /**
   * 节点下Api的Object的路径，例如：
   *
   * <ul>
   *   <li>节点是：{@link de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.HeadUpDisplay}
   *   <li>节点的名字是：{@link HeadUpDisplay#switchControls()}
   *   <li>节点的路径是：{@link de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.SwitchControlsApi}
   *   <li>return 节点下Api的Object的路径是：{@link
   *       de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.SwitchControlObject}
   * </ul>
   */
  private String apiObjectPath;

  /**
   * 节点下Api的Object的名字，例如：
   *
   * <ul>
   *   <li>节点是：{@link de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.HeadUpDisplay}
   *   <li>节点的名字是：{@link HeadUpDisplay#switchControls()}
   *   <li>节点的路径是：{@link de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.SwitchControlsApi}
   *   <li>节点下Api的Object的路径是：{@link
   *       de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.SwitchControlObject}
   *   <li>return 节点下Api的Object的名字是：{SwitchControlObject}
   * </ul>
   */
  private String apiObjectName;

  /**
   * api节点类中更新方法参数的名字 例如：
   * Api的类:de.esolutions.fw.rudi.viwi.service.hvacvehiclepreconditioning.v101.SwitchControlsApi
   * 其中的更新方法为:Single<URI> updateSwitchControlsObject(UUID var1, UpdateSwitchControlsObject var2)
   * 参数为：UpdateSwitchControlsObject，
   * 所以返回的名字为：de.esolutions.fw.rudi.viwi.service.hvacvehiclepreconditioning.v101
   */

  /**
   * 节点下Api的更新对象的包名，例如：
   *
   * <ul>
   *   <li>节点是：{@link de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.HeadUpDisplay}
   *   <li>节点的名字是：{@link HeadUpDisplay#switchControls()}
   *   <li>节点的路径是：{@link de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.SwitchControlsApi}
   *   <li>节点下Api的更新对象是：{@link
   *       de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.UpdateSwitchControlObject}
   *   <li>return 节点下Api的更新对象的包名是：{@link de.esolutions.fw.rudi.viwi.service.headupdisplay.v4}
   * </ul>
   */
  private String updateObjectPackage = "";

  /**
   * api节点类中更新方法参数的名字 例如：
   * Api的类:de.esolutions.fw.rudi.viwi.service.hvacvehiclepreconditioning.v101.SwitchControlsApi
   * 其中的更新方法为:Single<URI> updateSwitchControlsObject(UUID var1, UpdateSwitchControlsObject var2)
   * 参数为：UpdateSwitchControlsObject， 所以返回的名字为：UpdateSwitchControlsObject
   */

  /**
   * 节点下Api的更新对象的名字，例如：
   *
   * <ul>
   *   <li>节点是：{@link de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.HeadUpDisplay}
   *   <li>节点的名字是：{@link HeadUpDisplay#switchControls()}
   *   <li>节点的路径是：{@link de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.SwitchControlsApi}
   *   <li>节点下Api的更新对象是：{@link
   *       de.esolutions.fw.rudi.viwi.service.headupdisplay.v4.UpdateSwitchControlObject}
   *   <li>节点下Api的更新对象的包名是：{@link de.esolutions.fw.rudi.viwi.service.headupdisplay.v4}
   *   <li>节点下Api的更新对象的名字是：{UpdateSwitchControlObject}
   * </ul>
   */
  private String updateObjectName = "";

  public String getApiName() {
    return apiName;
  }

  public void setApiName(String apiName) {
    this.apiName = apiName;
  }

  public String getApiPath() {
    return apiPath;
  }

  public void setApiPath(String apiPath) {
    this.apiPath = apiPath;
  }

  public String getApiObjectPath() {
    return apiObjectPath;
  }

  public void setApiObjectPath(String apiObjectPath) {
    this.apiObjectPath = apiObjectPath;
  }

  public String getApiObjectName() {
    return apiObjectName;
  }

  public void setApiObjectName(String apiObjectName) {
    this.apiObjectName = apiObjectName;
  }

  public String getUpdateObjectPackage() {
    return updateObjectPackage;
  }

  public void setUpdateObjectPackage(String updateObjectPackage) {
    this.updateObjectPackage = updateObjectPackage;
  }

  public String getUpdateObjectName() {
    return updateObjectName;
  }

  public void setUpdateObjectName(String updateObjectName) {
    this.updateObjectName = updateObjectName;
  }

  @Override
  public String toString() {
    return "ApiNodeBean{"
        + "apiName='"
        + apiName
        + '\''
        + ", apiPath='"
        + apiPath
        + '\''
        + ", apiObjectPath='"
        + apiObjectPath
        + '\''
        + ", apiObjectName='"
        + apiObjectName
        + '\''
        + ", updateObjectPackage='"
        + updateObjectPackage
        + '\''
        + ", updateObjectName='"
        + updateObjectName
        + '\''
        + '}';
  }
}
