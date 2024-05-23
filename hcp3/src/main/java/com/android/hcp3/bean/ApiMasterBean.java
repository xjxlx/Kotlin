package com.android.hcp3.bean;

public class ApiMasterBean {
  /** api中master的文件名字，例如： */
  private String apiName;

  /** api中master的包名，例如： */
  private String apiPackage;

  /** 文件是否存在 */
  private boolean exists;

  public String getApiName() {
    return apiName;
  }

  public void setApiName(String apiName) {
    this.apiName = apiName;
  }

  public String getApiPackage() {
    return apiPackage;
  }

  public void setApiPackage(String apiPackage) {
    this.apiPackage = apiPackage;
  }

  public boolean isExists() {
    return exists;
  }

  public void setExists(boolean exists) {
    this.exists = exists;
  }

  @Override
  public String toString() {
    return "ApiMasterBean{"
        + "apiName='"
        + apiName
        + '\''
        + ", apiPackage='"
        + apiPackage
        + '\''
        + ", exists="
        + exists
        + '}';
  }
}
