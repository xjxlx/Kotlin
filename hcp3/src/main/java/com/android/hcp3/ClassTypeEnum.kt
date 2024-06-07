package com.android.hcp3;

public enum ClassTypeEnum {
  /** 无效的数据类型 */
  INVALID,

  /** 基础数据类型 */
  PRIMITIVE,

  /** list集合，泛型是基础数据类型 */
  LIST_PRIMITIVE,

  /** 自定义object类型数据 */
  OBJECT,

  /** 枚举类型数据 */
  ENUM,

  /** list集合，泛型是引用数据类型 */
  LIST_OBJECT,

  /** list集合，泛型是enum数据类型 */
  LIST_ENUM,

  /** 数组类型数据，暂时不用 */
  ARRAY
}
