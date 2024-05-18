package com.android.hcp3.rsi.hvac.valueindications;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.ValueIndicationObjectUnitPercentEnum;

public enum VcValueIndicationObjectUnitPercentEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  PERCENT("percent");

  private final String value;

  VcValueIndicationObjectUnitPercentEnum(String object) {
    this.value = object;
  }

  public static VcValueIndicationObjectUnitPercentEnum fromRSI(
      ValueIndicationObjectUnitPercentEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
