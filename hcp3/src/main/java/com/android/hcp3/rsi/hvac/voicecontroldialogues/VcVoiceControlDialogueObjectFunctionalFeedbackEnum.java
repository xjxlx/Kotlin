package com.android.hcp3.rsi.hvac.voicecontroldialogues;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.VoiceControlDialogueObjectFunctionalFeedbackEnum;

public enum VcVoiceControlDialogueObjectFunctionalFeedbackEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  AUTOMODEREQUIRED("autoModeRequired"),

  BADAQIOUTSIDE("badAQIOutside"),

  COMMANDALREADYGIVEN("commandAlreadyGiven"),

  COMMANDEXECUTED("commandExecuted"),

  COMMANDISPENDING("commandIsPending"),

  COOLDOWNACTIVE("cooldownActive"),

  ENGINENOTRUNNING("engineNotRunning"),

  FOGRISK("fogRisk"),

  FUNCTIONNOTINSTALLED("functionNotInstalled"),

  FUNCTIONNOTINSTALLEDUNLOCKABLE("functionNotInstalledUnlockable"),

  HEATUPACTIVE("heatUpActive"),

  IGNITIONISOFF("ignitionIsOff"),

  INIT("init"),

  MAXIMUMVALUEREACHED("maximumValueReached"),

  MINIMUMVALUEREACHED("minimumValueReached"),

  REJECTION("rejection"),

  VALUENOTPOSSIBLESETTINGMAXVALUE("valueNotPossibleSettingMaxValue"),

  VALUENOTPOSSIBLESETTINGMINVALUE("valueNotPossibleSettingMinValue");

  private final String value;

  VcVoiceControlDialogueObjectFunctionalFeedbackEnum(String object) {
    this.value = object;
  }

  public static VcVoiceControlDialogueObjectFunctionalFeedbackEnum fromRSI(
      VoiceControlDialogueObjectFunctionalFeedbackEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
