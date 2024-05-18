package com.android.hcp3.rsi.hvac;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchIndicationValue;

public enum VcSwitchIndicationValue {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  COOLING("cooling"),

  COOLINGANDHEATING("coolingAndHeating"),

  HEATING("heating"),

  OFF("off"),

  ON("on"),

  PASSIVEVENTILATING("passiveVentilating"),

  UNKNOWN("unknown");

  private final String value;

  VcSwitchIndicationValue(String object) {
    this.value = object;
  }

  public static VcSwitchIndicationValue fromRSI(SwitchIndicationValue enumObject) {
    return valueOf(enumObject.name());
  }
}
