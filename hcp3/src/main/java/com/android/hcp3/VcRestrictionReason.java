package com.android.hcp3;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.RestrictionReason;

public enum VcRestrictionReason {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  CLAMP15OFF("clamp15Off"),

  CLIMATISATIONACTIVE("climatisationActive"),

  DEFECT("defect"),

  ENGINE("engine"),

  ERROR("error"),

  FOOTRESTNOTREADY("footrestNotReady"),

  PRIORITYLOCK("priorityLock"),

  RANGEMODENOTACTIVE("rangeModeNotActive"),

  SPEEDOUTOFRANGE("speedOutOfRange"),

  SYSTEMOFF("systemOff"),

  UNKNOWN("unknown"),

  VEHICLESTANDSTILL("vehicleStandstill");

  private final String value;

  VcRestrictionReason(String object) {
    this.value = object;
  }

  public static VcRestrictionReason fromRSI(RestrictionReason enumObject) {
    return valueOf(enumObject.name());
  }
}
