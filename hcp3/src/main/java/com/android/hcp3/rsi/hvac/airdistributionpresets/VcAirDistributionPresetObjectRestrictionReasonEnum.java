package com.android.hcp3.rsi.hvac.airdistributionpresets;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.AirDistributionPresetObjectRestrictionReasonEnum;

public enum VcAirDistributionPresetObjectRestrictionReasonEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  CLAMP15OFF("clamp15Off"),

  DEFECTIVE("defective"),

  ENGINE("engine"),

  ERROR("error"),

  PRIORITYLOCK("priorityLock"),

  SPEEDOUTOFRANGE("speedOutOfRange"),

  SYSTEMOFF("systemOff"),

  UNKNOWN("unknown"),

  VEHICLESTANDSTILL("vehicleStandstill");

  private final String value;

  VcAirDistributionPresetObjectRestrictionReasonEnum(String object) {
    this.value = object;
  }

  public static VcAirDistributionPresetObjectRestrictionReasonEnum fromRSI(
      AirDistributionPresetObjectRestrictionReasonEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
