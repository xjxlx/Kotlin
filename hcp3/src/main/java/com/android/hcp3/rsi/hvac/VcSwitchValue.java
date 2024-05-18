package com.android.hcp3.rsi.hvac;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchValue;

public enum VcSwitchValue {
  _UNKNOWN_ENUM_VALUE("$UNKNOWN_ENUM_VALUE"),

  ALLTODRIVER("allToDriver"),

  AUTO("auto"),

  BODY("body"),

  BODYFOOTWELL("bodyFootwell"),

  BREATHE("breathe"),

  CHINA("china"),

  COMFORT("comfort"),

  COOLBOOST("coolBoost"),

  DEFAULT("default"),

  DEFAULTSUCCESSFUL("defaultSuccessful"),

  DRYBOOST("dryBoost"),

  ECONOMY("economy"),

  EUROPE("europe"),

  FOOTWELL("footwell"),

  HEATBOOST("heatBoost"),

  LEFT("left"),

  LEFTCENTRE("leftCentre"),

  MEDIUM("medium"),

  MOVIE("movie"),

  MUSIC("music"),

  NONE("none"),

  NORMAL("normal"),

  OFF("off"),

  ON("on"),

  ONE("one"),

  POWERNAP("powernap"),

  RANGE("range"),

  RECHARGE("recharge"),

  RELAX("relax"),

  RIGHT("right"),

  RIGHTCENTRE("rightCentre"),

  SELECTION("selection"),

  SETDEFAULT("setDefault"),

  SOUTHKOREA("southKorea"),

  STRONG("strong"),

  THREE("three"),

  TWO("two"),

  UNITEDSTATESEPA("unitedStatesEPA"),

  UP("up"),

  UPBODY("upBody"),

  UPBODYFOOTWELL("upBodyFootwell"),

  UPFOOTWELL("upFootwell"),

  VITALIZE("vitalize"),

  WEAK("weak"),

  WORKOUT("workout");

  private final String value;

  VcSwitchValue(String object) {
    this.value = object;
  }

  public static VcSwitchValue fromRSI(SwitchValue enumObject) {
    return valueOf(enumObject.name());
  }
}
