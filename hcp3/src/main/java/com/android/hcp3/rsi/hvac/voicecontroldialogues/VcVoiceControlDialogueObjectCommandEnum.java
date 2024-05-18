package com.android.hcp3.rsi.hvac.voicecontroldialogues;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.VoiceControlDialogueObjectCommandEnum;

public enum VcVoiceControlDialogueObjectCommandEnum {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  AC("aC"),

  ACTIVATEAUTOMODE("activateAutoMode"),

  AIRCIRCULATION("airCirculation"),

  AIRDISTRIBUTIONFEET("airDistributionFeet"),

  AIRDISTRIBUTIONPASSENGER("airDistributionPassenger"),

  AIRDISTRIBUTIONPASSENGERFEET("airDistributionPassengerFeet"),

  AIRDISTRIBUTIONWINDSHIELD("airDistributionWindshield"),

  AIRDISTRIBUTIONWINDSHIELDFEET("airDistributionWindshieldFeet"),

  AIRDISTRIBUTIONWINDSHIELDPASSENGER("airDistributionWindshieldPassenger"),

  AIRDISTRIBUTIONWINDSHIELDPASSENGERFEET("airDistributionWindshieldPassengerFeet"),

  AIRVOLUMEABSOLUTEVALUE("airVolumeAbsoluteValue"),

  AIRVOLUMERELATIVEVALUE("airVolumeRelativeValue"),

  BADSMELLDUSTPROTECTION("badSmellDustProtection"),

  CLIMATESTYLEABSOLUTEVALUE("climateStyleAbsoluteValue"),

  CLIMATESTYLERELATIVEVALUE("climateStyleRelativeValue"),

  DEACTIVATEDEMAND("deactivateDemand"),

  DEFOGWINDOW("defogWindow"),

  FEETARECOLD("feetAreCold"),

  FEETAREWARM("feetAreWarm"),

  FRESHAIR("freshAir"),

  FRONTWINDOWHEATER("frontWindowHeater"),

  HANDSARECOLD("handsAreCold"),

  HANDSAREWARM("handsAreWarm"),

  IAMCOLD("iAmCold"),

  IAMWARM("iAmWarm"),

  INIT("init"),

  LESSAIR("lessAir"),

  MAXAC("maxAc"),

  MAXDEFROST("maxDefrost"),

  MIRRORHEATER("mirrorHeater"),

  MOREAIR("moreAir"),

  OFFMODE("offMode"),

  PRECLIMATEIMMEDIATEHEATING("preClimateImmediateHeating"),

  PUREAIR("pureAir"),

  REARLOCK("rearLock"),

  REARWINDOWHEATER("rearWindowHeater"),

  SEATHEATING("seatHeating"),

  SEATHEATINGABSOLUTEVALUE("seatHeatingAbsoluteValue"),

  SEATHEATINGRELATIVEVALUE("seatHeatingRelativeValue"),

  SEATISCOLD("seatIsCold"),

  SEATISWARM("seatIsWarm"),

  SEATVENTILATION("seatVentilation"),

  SEATVENTILATIONABSOLUTEVALUE("seatVentilationAbsoluteValue"),

  SEATVENTILATIONRELATIVEVALUE("seatVentilationRelativeValue"),

  STEERINGWHEELHEATER("steeringWheelHeater"),

  STEERINGWHEELHEATERABSOLUTEVALUE("steeringWheelHeaterAbsoluteValue"),

  STEERINGWHEELHEATERRELATIVEVALUE("steeringWheelHeaterRelativeValue"),

  STEERINGWHEELISCOLD("steeringWheelIsCold"),

  STEERINGWHEELISWARM("steeringWheelIsWarm"),

  SYNCHRONISATION("synchronisation"),

  TEMPERATUREABSOLUTEVALUE("temperatureAbsoluteValue"),

  TEMPERATURERELATIVEVALUE("temperatureRelativeValue"),

  TOOLOUDTHEREISADRAUGHT("tooLoudThereIsADraught");

  private final String value;

  VcVoiceControlDialogueObjectCommandEnum(String object) {
    this.value = object;
  }

  public static VcVoiceControlDialogueObjectCommandEnum fromRSI(
      VoiceControlDialogueObjectCommandEnum enumObject) {
    return valueOf(enumObject.name());
  }
}
