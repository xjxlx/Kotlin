package com.android.hcp3.rsi.hvac.flavourcartridges;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.FlavourCartridgeObjectFlavourEnum;

public enum VcFlavourCartridgeObjectFlavourEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  SUMMER("summer"),

  WINTER("winter");

  private final String value;

  VcFlavourCartridgeObjectFlavourEnum(String object) {
    this.value = object;
  }

  public static VcFlavourCartridgeObjectFlavourEnum fromRSI(
      FlavourCartridgeObjectFlavourEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
