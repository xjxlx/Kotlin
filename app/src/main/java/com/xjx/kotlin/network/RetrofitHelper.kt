package com.xjx.kotlin.network

import com.android.helper.httpclient.RetrofitHelper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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

    val api by lazy {
        val builder = OkHttpClient.Builder()

        // set connect time
        builder
            .readTimeout(CONNECT_TIME, TimeUnit.SECONDS) //设置读取超时时间
            .writeTimeout(CONNECT_TIME, TimeUnit.SECONDS) //设置写的超时时间
            .connectTimeout(CONNECT_TIME, TimeUnit.SECONDS) //设置连接超时时间

        // add  interceptor
        if (mListInterceptor.size > 0) {
            mListInterceptor.forEach {
                builder.addInterceptor(it)
            }
        }

    }

    /**
     * add  connect interceptor
     */
    fun addInterceptor(interceptor: Interceptor) {
        mListInterceptor.add(interceptor)
    }

}

fun setSSLFactory(builder: OkHttpClient.Builder?) {

    // 校验SSL
    val mX509TrustManager: X509TrustManager = object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }

    // verify host
    val mHostnameVerifier = HostnameVerifier { hostname: String?, session: SSLSession? -> true }

    // 设置https 访问的时候对所有证书都进行信任
    if (builder != null) {
        try {
            val sslContext = SSLContext.getInstance("SSL")
            if (sslContext != null) {
                sslContext.init(null, arrayOf<TrustManager?>(mX509TrustManager), SecureRandom())
                RetrofitHelper.mSslSocketFactory = sslContext.socketFactory
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 信任所有的证书
        if (RetrofitHelper.mSslSocketFactory != null && mX509TrustManager != null) {
            builder.sslSocketFactory(RetrofitHelper.mSslSocketFactory, mX509TrustManager)
        }

        // 不校验主机名
        if (mHostnameVerifier != null) {
            builder.hostnameVerifier(mHostnameVerifier)
        }
    }
}