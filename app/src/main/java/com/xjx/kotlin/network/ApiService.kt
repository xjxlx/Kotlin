package com.xjx.kotlin.network

import com.xjx.kotlin.network.bean.ConsecrateListBean
import com.xjx.kotlin.network.bean.UserInfoBean
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author : 流星
 * @CreateDate: 2023/3/14-18:17
 * @Description:
 */
interface ApiService {

    /**
     * 获取用户信息
     */
    @GET("test-api/mobile/getInfo")
    suspend fun getUserInfo(): HttpResult<UserInfoBean>

    /**
     * 获取用户信息
     */
    @GET("test-api/mobile/getInfo")
    suspend fun getUserInfo2(name: Int): HttpResult<UserInfoBean>

    /**
     * 获取供奉列表数据
     */
    @POST("test-api/service/consecrate/list")
    suspend fun getConsecrateList(@Body body: RequestBody): Flow<HttpResult<ConsecrateListBean>>
}