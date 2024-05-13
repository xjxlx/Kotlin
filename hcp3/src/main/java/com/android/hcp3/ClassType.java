package com.android.hcp3;

public enum ClassType {
  /** 无效的数据类型 */
  INVALID,

  /** 基础数据类型 */
  PRIMITIVE,

  /** 自定义object类型数据 */
  OBJECT,

  /** list集合，泛型是基础数据类型 */
  LIST_PRIMITIVE,

  /** list集合，泛型是引用数据类型 */
  LIST_OBJECT,

  /** list集合，泛型是enum数据类型 */
  LIST_ENUM,

  /** 枚举类型数据 */
  ENUM,

  /** 数组类型数据，暂时不用 */
  ARRAY
}
