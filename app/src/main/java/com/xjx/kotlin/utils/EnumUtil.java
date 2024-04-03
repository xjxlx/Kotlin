package com.xjx.kotlin.utils;

import java.io.*;
import java.lang.reflect.Field;

public class EnumUtil {

  public static <T extends Enum<T>, R extends Enum<R>> void convert(
      Class<T> targetCls, Class<R> originCls) {
    StringBuilder sb = new StringBuilder();
    String targetClassName = targetCls.getSimpleName();
    String originClassName = originCls.getSimpleName();
    // System.out.println("originClassName:clsName:" + originClassName);

    sb.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        .append("\r\n")
        .append("\r\n");

    // 增加源码中的属性
    Field[] fields = originCls.getFields();
    for (Field field : fields) {
      if (field.getType().isEnum()) {
        String fieldName = field.getName();
        sb.append("  ")
            .append(fieldName)
            .append("(")
            .append(originClassName)
            .append(".")
            .append(fieldName)
            .append(".name())")
            .append(",")
            .append("\r\n");
      }
    }
    if (sb.length() > 3) {
      sb.delete(sb.length() - 3, sb.length());
      sb.append(";");
    }

    sb.append("\r\n")
        .append("\r\n")
        .append(" public static ")
        .append(targetClassName)
        .append(" fromRSI (@NonNull ")
        .append(originClassName)
        .append(" enum) {")
        .append("\r\n")
        .append("     return  ")
        .append(targetClassName)
        .append(".valueOf(enum.name());")
        .append("\r\n")
        .append(" }");

    sb.append("\r\n")
        .append("\r\n")
        .append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        .append("\r\n");
    System.out.println(sb);
  }
}
