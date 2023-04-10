package com.xjx.kotlin.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.DownCountTime
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityNetWorkBinding
import com.xjx.kotlin.network.ApiLogic.UserLogic
import com.xjx.kotlin.network.bean.UserInfoBean
import com.xjx.kotlin.network.listener.HttpCallBackListener
import com.xjx.kotlin.ui.activity.test.Api
import com.xjx.kotlin.ui.activity.test.aaa
import com.xjx.kotlin.ui.activity.test.fx.TestFx1
import kotlinx.coroutines.launch

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
            HttpClient.http(block = {
                UserLogic.getUser()
            }, listener = object : HttpCallBackListener<HttpResult<UserInfoBean>> {
                override fun onSuccess(t: HttpResult<UserInfoBean>) {

                }

                override fun onError(throwable: Throwable) {

                }
            })
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

//        HttpClient.http(ApiService::getUserInfo<com.xjx.kotlin.network.bean.UserInfoBean>(), this::getTest)

//
//        test {
//
//        }

//        test(  {ApiService::getUserInfo},"")
//        test(ApiService::getUserInfo(), "")

    }

    suspend inline fun <reified T, F, R> http(block: T.(F) -> HttpResult<R>, b: F): HttpResult<R> {
        val apiService = RetrofitHelper.create(T::class.java)
        apiService.block(b)
        return apiService.block(b)
    }

    fun test(block: () -> HttpResult<UserInfoBean>, arg: String) {

    }

}