package com.xjx.kotlin.network.bean

/**
 * @author : 流星
 * @CreateDate: 2022/11/29-01:10
 * @Description:
 */
data class UserInfoBean(
    val userId: Long = 0,
    val userName: String = "",
    val nickName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val sex: String = "",
    val avatar: String = "",
    val status: String = "",
    val loginIp: String = "",
    val loginDate: String = ""
)