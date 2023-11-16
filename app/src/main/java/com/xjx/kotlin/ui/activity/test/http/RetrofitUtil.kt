package com.xjx.kotlin.ui.activity.test.http

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * @author : 流星
 * @CreateDate: 2022/9/4-20:51
 * @Description:
 */
object RetrofitUtil {

    fun <T> getApiService(service: Class<T>): T {
        return Retrofit.Builder().baseUrl("https://web.jollyeng.com/").addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
            .create(service)
    }
}