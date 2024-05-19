

//
package com.android.hcp3;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.SpecialValue;

public enum VcSpecialValue {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  ABSOLUTEDATE("absoluteDate"),

  AUTO("auto"),

  ECO("eco"),

  HIGH("high"),

  INFINITE("infinite"),

  LOW("low"),

  NEXTOCCURENCE("nextOccurence"),

  OFF("off"),

  ON("on"),

  WEEKDAYS("weekDays");

  private final String value;

  VcSpecialValue(String object) {
    this.value = object;
  }

  public static VcSpecialValue fromRSI(SpecialValue enumObject) {
    return valueOf(enumObject.name());
  }
}
