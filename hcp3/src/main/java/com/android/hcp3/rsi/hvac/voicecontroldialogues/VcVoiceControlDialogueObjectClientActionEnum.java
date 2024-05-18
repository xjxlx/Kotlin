package com.android.hcp3.rsi.hvac.voicecontroldialogues;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.VoiceControlDialogueObjectClientActionEnum;

public enum VcVoiceControlDialogueObjectClientActionEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  DISCARDDIALOGUE("discardDialogue"),

  NEGATIVERESPONSE("negativeResponse"),

  POSITIVERESPONSE("positiveResponse");

  private final String value;

  VcVoiceControlDialogueObjectClientActionEnum(String object) {
    this.value = object;
  }

  public static VcVoiceControlDialogueObjectClientActionEnum fromRSI(
      VoiceControlDialogueObjectClientActionEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
