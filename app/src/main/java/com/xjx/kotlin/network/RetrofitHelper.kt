package com.xjx.kotlin.network

import android.annotation.SuppressLint
import android.text.TextUtils
import com.android.helper.utils.LogUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * @author : 流星
 * @CreateDate: 2023/3/14-18:18
 * @Description:
 */
object RetrofitHelper {

    private const val CONNECT_TIME = 30L
    private val mListInterceptor = arrayListOf<Interceptor>()
    private val retrofit: Retrofit by lazy {
        val builder = OkHttpClient.Builder().apply {
            // set connect time
            readTimeout(CONNECT_TIME, TimeUnit.SECONDS) //设置读取超时时间
            writeTimeout(CONNECT_TIME, TimeUnit.SECONDS) //设置写的超时时间
            connectTimeout(CONNECT_TIME, TimeUnit.SECONDS) //设置连接超时时间

            // add  interceptor
            if (mListInterceptor.size > 0) {
                mListInterceptor.forEach {
                    addInterceptor(it)
                }
            }
        }

        // set  ssl certificates
        setSSLFactory(builder)

        Retrofit.Builder().apply {
            client(builder.build())
            if (TextUtils.isEmpty(mBaseUrl)) {
                LogUtil.e("Retrofit Base url is null !")
            } else {
                baseUrl(mBaseUrl)
            }
            addConverterFactory(GsonConverterFactory.create())
            addConverterFactory(ScalarsConverterFactory.create())
        }.build()
    }
    private var mBaseUrl: String = ""

    /**
     * add  connect interceptor
     */
    fun addInterceptor(interceptor: Interceptor) {
        mListInterceptor.add(interceptor)
    }

    /**
     * set base url path
     */
    fun setBaseUrl(baseUrl: String) {
        this.mBaseUrl = baseUrl
    }

    /**
     * create a service
     */
    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    @SuppressLint("TrustAllX509TrustManager,BadHostnameVerifier,CustomX509TrustManager")
    private fun setSSLFactory(builder: OkHttpClient.Builder?) {
        builder?.let {
            var mSslSocketFactory: SSLSocketFactory? = null

            val mX509TrustManager: X509TrustManager = object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            }
            val mHostnameVerifier = HostnameVerifier { _: String?, _: SSLSession? -> true }

            try {
                val sslContext = SSLContext.getInstance("SSL")
                if (sslContext != null) {
                    sslContext.init(null, arrayOf<TrustManager?>(mX509TrustManager), SecureRandom())
                    mSslSocketFactory = sslContext.socketFactory
                }

                // trust all https certificates
                if (mSslSocketFactory != null) {
                    builder.sslSocketFactory(mSslSocketFactory, mX509TrustManager)
                }

                // trust all host
                builder.hostnameVerifier(mHostnameVerifier)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
