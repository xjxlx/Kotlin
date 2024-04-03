package com.xjx.kotlin.ui.activity.feature;

public enum OriginEnum {
  YEAR("year"),
  MONTH("month"),
  DAY("day"),
  HOUR("hour"),
  MINUTE("minute"),
  SECOND("second");

  private final String date;

  OriginEnum(String date) {
    this.date = date;
  }
}
