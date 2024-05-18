package com.android.hcp3.rsi.hvac.voicecontroldialogues;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.VoiceControlDialogueObjectSwitchValueEnum;

public enum VcVoiceControlDialogueObjectSwitchValueEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  ACTIVATEINADDITION("activateInAddition"),

  ACTIVE("active"),

  HIGH("high"),

  INACTIVE("inactive"),

  INTENSIVE("intensive"),

  LOW("low"),

  MEDIUM("medium"),

  SOFT("soft");

  private final String value;

  VcVoiceControlDialogueObjectSwitchValueEnum(String object) {
    this.value = object;
  }

  public static VcVoiceControlDialogueObjectSwitchValueEnum fromRSI(
      VoiceControlDialogueObjectSwitchValueEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
