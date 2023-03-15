package com.xjx.kotlin.network.bean

data class Row(
    val id: Long = 0,
    val categoryName: String = "",
    val secondName: String = "",
    val tabletStatus: String = "",
    val consecrateName: String = "",
    val consecrateStart: String = "",
    val consecrateEnd: String = "",
    val wallName: String = "",
    val palaceName: String = "",
    val location: String = "",
    val consecrateAmount: Double = 0.0,
    val createBy: String = ""
)