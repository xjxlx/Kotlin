package com.xjx.kotlin.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.android.helper.utils.DownCountTime
import com.android.http.client.HttpClient
import com.android.http.client.HttpResult
import com.android.http.client.RetrofitHelper
import com.android.http.listener.HttpCallBackListener
import com.android.http.test.HttpResponse
import com.android.http.test.L6HomeRightBookListBean
import com.android.http.test.TestApiService
import com.xjx.kotlin.databinding.ActivityNetWorkBinding
import com.xjx.kotlin.network.bean.UserInfoBean
import com.xjx.kotlin.ui.activity.test.fx.TestFx1
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** 网络封装 */
class NetWorkActivity : BaseBindingTitleActivity<ActivityNetWorkBinding>() {

    override fun getTitleContent(): String {
        return "网络封装"
    }

    override fun initData(savedInstanceState: Bundle?) {
        RetrofitHelper.setBaseUrl("https://web.jollyeng.com/")

        val downCountTime = DownCountTime()
        downCountTime.setCountdown(5, 1000, object : DownCountTime.CallBack {
            override fun onTick(current: Long, countdown: Long) {
                LogUtil.e("DOWN--- ", "current: $countdown count: $current")
            }

            override fun onFinish() {
                LogUtil.e("DOWN--- ", "onFinish: ")
            }
        })

        mBinding.btnStart.setOnClickListener {
            lifecycleScope.launch {
                val unId = "o9RWl1EJPHolk8_7smU39k1-LqVs"
                val suiJi = "newcL6_2"
                val mParameters = mutableMapOf<String, Any>()
                mParameters["service"] = "App.App2022.GetBook"
                mParameters["unid"] = unId
                mParameters["suiji"] = suiJi

                HttpClient.http<TestApiService, MutableMap<String, Any>, HttpResponse<L6HomeRightBookListBean>>({ getL6BookList(it) },
                    mParameters, object : HttpCallBackListener<HttpResponse<L6HomeRightBookListBean>>() {
                        override fun onFailure(exception: Throwable) {
                            super.onFailure(exception)
                            LogUtil.e("sss", "error:${exception}")
                        }

                        override fun onSuccess(t: HttpResponse<L6HomeRightBookListBean>) {
                            LogUtil.e("sss", t)
                        }
                    })
            }
        }
        mBinding.btnPause.setOnClickListener { downCountTime.pause() }
        mBinding.btnResume.setOnClickListener { downCountTime.resume() }

        TestFx1.main(null)

        val format1: String = DateFormat.getDateTimeInstance().format(Date())
        val format = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CHINA).format(Date())
        LogUtil.e("dateFormat: $format1")
        LogUtil.e("dateFormat: $format")
    }

    inline fun <reified T, F, R> http(block: T.(F) -> HttpResult<R>, b: F): HttpResult<R> {
        val apiService = RetrofitHelper.create(T::class.java)
        return apiService.block(b)
    }

    //    T.()->Unit，(T) -> Unit，() -> Unit
    private fun <T, R, F> T.myApply(block: T.(F) -> HttpResult<R>?, b: F) {
        block(b)
    }

    private fun <T> T.myAlso(block: (T) -> Unit) {
        block(this)
    }

    private fun <T, R> T.apply2(block: T.(R) -> HttpResult<R>?, b: R) {
        block(b)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ): ActivityNetWorkBinding {
        return ActivityNetWorkBinding.inflate(inflater, container, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = User()
        //
        //        http<ApiService, Int, UserInfoBean>({
        //            getUserInfo2(it)
        //        }, 3)

        user.apply4<User, Int, UserInfoBean>({ test2() }, 34)
    }

    private fun <T, P, R> T.apply4(block: T.(P) -> HttpResult<R>, p: P) {
        this.block(p)
    }

    class User {
        var name = "张三"
        fun test2(): HttpResult<UserInfoBean> {
            return HttpResult()
        }
    }
}
