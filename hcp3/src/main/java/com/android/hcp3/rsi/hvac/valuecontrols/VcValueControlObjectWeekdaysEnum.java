package com.android.hcp3.rsi.hvac.valuecontrols;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.ValueControlObjectWeekdaysEnum;

public enum VcValueControlObjectWeekdaysEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  FRIDAY("friday"),

  MONDAY("monday"),

  SATURDAY("saturday"),

  SUNDAY("sunday"),

  THURSDAY("thursday"),

  TUESDAY("tuesday"),

  WEDNESDAY("wednesday");

  private final String value;

  VcValueControlObjectWeekdaysEnum(String object) {
    this.value = object;
  }

  public static VcValueControlObjectWeekdaysEnum fromRSI(
      ValueControlObjectWeekdaysEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
