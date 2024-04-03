package com.xjx.kotlin.ui.activity.feature;

public enum Date {
  YEAR("year"),
  MONTH("month"),
  DAY("day"),
  HOUR("hour"),
  MINUTE("minute"),
  SECOND("second");

  private final String date;

  Date(String date) {
    this.date = date;
  }
}
