package com.xjx.kotlin.network

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TestApi {

//    @FormUrlEncoded

    //    @Headers("Token:bf91bcac684fbdfbd3732fb3ac3de388c84a8260-1696787094805-a2e940357a9c46afb74b91e6")
    @POST("plugin/io.github.xjxlx.publish/1.4.5/delete")
    fun deletePlugin(@Body requestBody: RequestBody): Call<String>
}