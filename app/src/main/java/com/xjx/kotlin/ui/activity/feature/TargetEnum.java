package com.xjx.kotlin.ui.activity.feature;

public enum TargetEnum {
  YEAR("year"),
  MONTH("month"),
  DAY("day"),
  HOUR("hour"),
  MINUTE("minute"),
  SECOND("second");

  private final String date;

  TargetEnum(String date) {
    this.date = date;
  }
}
