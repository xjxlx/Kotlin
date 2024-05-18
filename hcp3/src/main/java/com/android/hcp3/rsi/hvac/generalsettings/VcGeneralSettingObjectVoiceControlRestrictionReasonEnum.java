package com.android.hcp3.rsi.hvac.generalsettings;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObjectVoiceControlRestrictionReasonEnum;

public enum VcGeneralSettingObjectVoiceControlRestrictionReasonEnum {
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

  VcGeneralSettingObjectVoiceControlRestrictionReasonEnum(String object) {
    this.value = object;
  }

  public static VcGeneralSettingObjectVoiceControlRestrictionReasonEnum fromRSI(
      GeneralSettingObjectVoiceControlRestrictionReasonEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
