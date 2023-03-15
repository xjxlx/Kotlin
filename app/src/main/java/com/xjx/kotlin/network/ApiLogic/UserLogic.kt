package com.xjx.kotlin.network.ApiLogic

import com.xjx.kotlin.network.ApiService
import com.xjx.kotlin.network.HttpResult
import com.xjx.kotlin.network.RetrofitHelper
import com.xjx.kotlin.network.bean.UserInfoBean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

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
}