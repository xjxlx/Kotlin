package com.android.hcp3.rsi.hvac.valueindications;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.ValueIndicationObjectUnitDensityEnum;

public enum VcValueIndicationObjectUnitDensityEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  MICROGRAM_PER_CUBICMETER("microgram_per_cubicMeter");

  private final String value;

  VcValueIndicationObjectUnitDensityEnum(String object) {
    this.value = object;
  }

  public static VcValueIndicationObjectUnitDensityEnum fromRSI(
      ValueIndicationObjectUnitDensityEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
