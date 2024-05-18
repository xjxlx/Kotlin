package com.android.hcp3.rsi.hvac.generalsettings;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObjectDriverSideEnum;

public enum VcGeneralSettingObjectDriverSideEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  LEFT("left"),

  RIGHT("right");

  private final String value;

  VcGeneralSettingObjectDriverSideEnum(String object) {
    this.value = object;
  }

  public static VcGeneralSettingObjectDriverSideEnum fromRSI(
      GeneralSettingObjectDriverSideEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
