package com.xjx.kotlin.ui.activity.test.nl

/**
 * @author : 流星
 * @CreateDate: 2023/3/27-23:53
 * @Description: 测试内联类
 */
class TestNl {
    fun testNL(value: LineTimes) {
    }

    fun test() {
        testNL(Type.value_1)
    }
}

inline class LineTimes(private val value: Int) {
    // 可以设置变量
    val toString: String
        get() = "$value"

    // 可以设置方法
    fun toHours(): Int {
        return value * 60
    }

    fun toMinutes(): Int {
        return value * 60 * 60
    }
}

object Type {
    val value_1 = LineTimes(1)
    val value_2 = LineTimes(2)
}