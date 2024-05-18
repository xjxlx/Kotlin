package com.android.hcp3.rsi.hvac;

import de.esolutions.fw.rudi.services.rsiglobal.TemperatureUnit;

public enum VcTemperatureUnit {
  KELVIN("K"),

  FAHRENHEIT("F"),

  CELSIUS("C");

  private final String value;

  VcTemperatureUnit(String object) {
    this.value = object;
  }

  public static VcTemperatureUnit fromRSI(TemperatureUnit enumObject) {
    return valueOf(enumObject.name());
  }
}
