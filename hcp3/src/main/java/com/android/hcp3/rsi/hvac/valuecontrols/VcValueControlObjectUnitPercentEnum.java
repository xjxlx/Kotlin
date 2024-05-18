package com.android.hcp3.rsi.hvac.valuecontrols;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.ValueControlObjectUnitPercentEnum;

public enum VcValueControlObjectUnitPercentEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  PERCENT("percent");

  private final String value;

  VcValueControlObjectUnitPercentEnum(String object) {
    this.value = object;
  }

  public static VcValueControlObjectUnitPercentEnum fromRSI(
      ValueControlObjectUnitPercentEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
