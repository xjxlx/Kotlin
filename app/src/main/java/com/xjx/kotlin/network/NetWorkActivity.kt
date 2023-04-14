package com.xjx.kotlin.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.httpclient.kotlin.RetrofitHelper
import com.android.helper.utils.DownCountTime
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityNetWorkBinding
import com.xjx.kotlin.network.bean.UserInfoBean
import com.xjx.kotlin.ui.activity.test.fx.TestFx1
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 网络封装
 */
class NetWorkActivity : AppBaseBindingTitleActivity<ActivityNetWorkBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityNetWorkBinding {
        return ActivityNetWorkBinding.inflate(inflater, container, true)
    }

    override fun setTitleContent(): String {
        return "网络封装"
    }

    override fun initData(savedInstanceState: Bundle?) {
        val downCountTime = DownCountTime()
        downCountTime.setCountdown(5, 1000, object : DownCountTime.CallBack {
            override fun onTick(count: Long, current: Long) {
                LogUtil.e("DOWN--- ", "current: " + current + " count: " + count)
            }

            override fun onFinish() {
                LogUtil.e("DOWN--- ", "onFinish: ")
            }
        })

        lifecycleScope.launch {
//            HttpClient.http(block = {
//                UserLogic.getUser()
//            }, listener = object : HttpCallBackListener<HttpResult<UserInfoBean>> {
//                override fun onSuccess(t: HttpResult<UserInfoBean>) {
//
//                }
//
//                override fun onError(throwable: Throwable) {
//
//                }
//            })
        }

        mBinding.btnStart.setOnClickListener {
            downCountTime.start()
        }
        mBinding.btnPause.setOnClickListener {
            downCountTime.pause()
        }
        mBinding.btnResume.setOnClickListener {
            downCountTime.resume()
        }

        TestFx1.main(null)

        val format1: String = DateFormat.getDateTimeInstance()
            .format(Date())
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

    fun <T, U> myApply2(block: T.(U) -> HttpResult<U>, b: Int) {
    }

    fun <T, F> myApply3(block: T.(F) -> HttpResult<F>, b: F) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = User()
//
//        http<ApiService, Int, UserInfoBean>({
//            getUserInfo2(it)
//        }, 3)

        user.apply4<User, Int, UserInfoBean>({
            test2(it)
        }, 34)
    }

    private fun <T, P, R> T.apply4(block: T.(P) -> HttpResult<R>, p: P) {
        this.block(p)
    }

    class User {
        var name = "张三"
        fun test2(agt: Int): HttpResult<UserInfoBean> {


            return HttpResult()

        }
    }

}