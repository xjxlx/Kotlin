package com.android.hcp3.rsi.hvac;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.SpecialIndicationValue;

public enum VcSpecialIndicationValue {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  INITIALIZE("initialize"),

  LOW("low"),

  NOTAVAILABLE("notAvailable"),

  OPTIMIZED("optimized");

  private final String value;

  VcSpecialIndicationValue(String object) {
    this.value = object;
  }

  public static VcSpecialIndicationValue fromRSI(SpecialIndicationValue enumObject) {
    return valueOf(enumObject.name());
  }
}
