package com.android.hcp3.rsi.hvac.flavourcartridges;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.FlavourCartridgeObjectIntensityValueConfigurationEnum;

public enum VcFlavourCartridgeObjectIntensityValueConfigurationEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  LIGHT("light"),

  MEDIUM("medium"),

  STRONG("strong"),

  SUBTLE("subtle");

  private final String value;

  VcFlavourCartridgeObjectIntensityValueConfigurationEnum(String object) {
    this.value = object;
  }

  public static VcFlavourCartridgeObjectIntensityValueConfigurationEnum fromRSI(
      FlavourCartridgeObjectIntensityValueConfigurationEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
