package com.xjx.kotlin.ui.activity.test.http

import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author : 流星
 * @CreateDate: 2022/9/4-20:50
 * @Description:
 */
interface TestApi {

    @POST(".")
    @FormUrlEncoded
    fun getTestList(@FieldMap map: Map<String, String>): Call<String>
}