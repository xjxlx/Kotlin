package com.android.hcp3.rsi.hvac.voicecontroldialogues;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.VoiceControlDialogueObjectZoneEnum;

public enum VcVoiceControlDialogueObjectZoneEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  FRONTROWLEFTSIDE("frontRowLeftSide"),

  FRONTROWRIGHTSIDE("frontRowRightSide"),

  REARROW1LEFTSIDE("rearRow1LeftSide"),

  REARROW1RIGHTSIDE("rearRow1RightSide"),

  REARROW2LEFTSIDE("rearRow2LeftSide"),

  REARROW2RIGHTSIDE("rearRow2RightSide");

  private final String value;

  VcVoiceControlDialogueObjectZoneEnum(String object) {
    this.value = object;
  }

  public static VcVoiceControlDialogueObjectZoneEnum fromRSI(
      VoiceControlDialogueObjectZoneEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
