package com.xjx.kotlin.network.bean

/**
 * @author : 流星
 * @CreateDate: 2022/11/25-23:06
 * @Description:
 */
data class ConsecrateListBean(
    val total: Int = 0,
    val rows: List<Row> = listOf(),
    val code: Int = 0,
    val msg: String = ""
)