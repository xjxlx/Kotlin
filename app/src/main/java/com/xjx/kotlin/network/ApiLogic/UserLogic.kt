package com.xjx.kotlin.network.ApiLogic

import com.android.apphelper2.utils.httpclient.HttpResult
import com.android.apphelper2.utils.httpclient.RetrofitHelper
import com.xjx.kotlin.network.ApiService
import com.xjx.kotlin.network.bean.UserInfoBean

/**
 * @author : 流星
 * @CreateDate: 2023/3/15-10:30
 * @Description:
 */
object UserLogic {

    suspend fun getUser(): HttpResult<UserInfoBean> {
        return RetrofitHelper
            .create(ApiService::class.java)
            .getUserInfo()
    }

    suspend fun getUser2(name: String): HttpResult<UserInfoBean> {
        return RetrofitHelper
            .create(ApiService::class.java)
            .getUserInfo()
    }
}