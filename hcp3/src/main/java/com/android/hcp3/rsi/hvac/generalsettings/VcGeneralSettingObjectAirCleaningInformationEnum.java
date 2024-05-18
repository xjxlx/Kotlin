package com.android.hcp3.rsi.hvac.generalsettings;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObjectAirCleaningInformationEnum;

public enum VcGeneralSettingObjectAirCleaningInformationEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  CARBODYSHELLISOPEN("carBodyShellIsOpen"),

  DOORISOPEN("doorIsOpen"),

  MANUALMODEISACTIVE("manualModeIsActive"),

  ROOFISOPEN("roofIsOpen"),

  WINDOWFOGGINGRISK("windowFoggingRisk"),

  WINDOWISOPEN("windowIsOpen");

  private final String value;

  VcGeneralSettingObjectAirCleaningInformationEnum(String object) {
    this.value = object;
  }

  public static VcGeneralSettingObjectAirCleaningInformationEnum fromRSI(
      GeneralSettingObjectAirCleaningInformationEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
