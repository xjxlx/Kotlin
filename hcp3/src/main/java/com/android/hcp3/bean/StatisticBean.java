package com.android.hcp3.bean;

import java.util.LinkedHashSet;

public class StatisticBean {

  /*
   * 父类节点的路径，例如：hcp3/src/main/java/com/android/hcp3/rsi/hvac/airoutlets
   * */
  private String apiNodePath;

  /*
   * 父类节点下泛型类的路径，例如：hcp3/src/main/java/com/android/hcp3/rsi/hvac/airoutlets/AirOutletEntity.java
   * */
  private String apiChildPath;

  /** 用于存储当前api节点object中的泛型类型,这里存放的是包名 */
  private LinkedHashSet<String> objectGenericSet;

  public String getApiNodePath() {
    return apiNodePath;
  }

  public void setApiNodePath(String apiNodePath) {
    this.apiNodePath = apiNodePath;
  }

  public String getApiChildPath() {
    return apiChildPath;
  }

  public void setApiChildPath(String apiChildPath) {
    this.apiChildPath = apiChildPath;
  }

  public LinkedHashSet<String> getObjectGenericSet() {
    return objectGenericSet;
  }

  public void setObjectGenericSet(LinkedHashSet<String> objectGenericSet) {
    this.objectGenericSet = objectGenericSet;
  }

  @Override
  public String toString() {
    return "StatisticBean{"
        + "apiNodePath='"
        + apiNodePath
        + '\''
        + ", apiChildPath='"
        + apiChildPath
        + '\''
        + ", objectGenericSet="
        + objectGenericSet
        + '}';
  }
}
