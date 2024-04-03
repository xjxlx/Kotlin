package com.xjx.kotlin.utils;

import java.io.*;
import java.lang.reflect.Field;

public class EnumUtil {

  public static <T extends Enum<T>> void convert(Class<T> originCls) {
    StringBuilder sb = new StringBuilder();
    String originClassName = originCls.getSimpleName();
    // System.out.println("originClassName:clsName:" + originClassName);

    // 增加源码中的属性
    Field[] fields = originCls.getFields();
    for (Field field : fields) {
      if (field.getType().isEnum()) {
        String fieldName = field.getName();
        sb.append(fieldName)
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
    System.out.println(sb);
  }
}
