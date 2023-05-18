package com.xjx.kotlin.bean

/**
 * @author : 流星
 * @CreateDate: 2023/1/6-09:52
 * @Description:
 */
data class ZmqBean(val heart_rate: Int = 0, val SaO2: Int = 0, val respiratory_rate: Int = 0, val respiratory_state: Int = 0,
    val time: Long = 0, val time_millis: Long = 0)
