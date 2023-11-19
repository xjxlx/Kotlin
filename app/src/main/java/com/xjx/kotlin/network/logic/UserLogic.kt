package com.xjx.kotlin.network.logic

import com.android.http.client.HttpResult
import com.android.http.client.RetrofitHelper
import com.xjx.kotlin.network.ApiService
import com.xjx.kotlin.network.bean.UserInfoBean

/**
 * @author : 流星
 * @CreateDate: 2023/3/15-10:30
 * @Description:
 */
object UserLogic {

    suspend fun getUser(): HttpResult<UserInfoBean> {
        return RetrofitHelper.create(ApiService::class.java).getUserInfo()
    }

    suspend fun getUser2(): HttpResult<UserInfoBean> {
        return RetrofitHelper.create(ApiService::class.java).getUserInfo()
    }
}