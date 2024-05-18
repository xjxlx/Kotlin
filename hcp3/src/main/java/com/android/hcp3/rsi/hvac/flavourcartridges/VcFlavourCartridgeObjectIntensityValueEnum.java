package com.android.hcp3.rsi.hvac.flavourcartridges;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.FlavourCartridgeObjectIntensityValueEnum;

public enum VcFlavourCartridgeObjectIntensityValueEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  LIGHT("light"),

  MEDIUM("medium"),

  STRONG("strong"),

  SUBTLE("subtle");

  private final String value;

  VcFlavourCartridgeObjectIntensityValueEnum(String object) {
    this.value = object;
  }

  public static VcFlavourCartridgeObjectIntensityValueEnum fromRSI(
      FlavourCartridgeObjectIntensityValueEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
