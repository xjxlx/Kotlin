package com.xjx.kotlin.ui.activity.test.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.httpclient.kotlin.HttpClient
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityFlowCallBinding
import com.xjx.kotlin.network.ApiService
import com.xjx.kotlin.network.bean.UserInfoBean
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class FlowCallActivity : AppBaseBindingTitleActivity<ActivityFlowCallBinding>() {

    override fun setTitleContent(): String {
        return "Flow Call"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityFlowCallBinding {
        return ActivityFlowCallBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

        val flow = callbackFlow {
            requestApi {
                // 发送成功的数据
                trySend("2")

                // 发送错误的数据
                close(NullPointerException("error"))
            }

            // 关闭的回调
            awaitClose {
                // 关闭的回调
                LogUtil.e("awaitClose ---->")
            }
        }


        lifecycleScope.launch {
            // 直接使用，会报错，flow如果发送数据的时候，必须要在携程体内发送，否则或报错
//            val flow = flow {
//                //模拟网络请求
//                requestApi {
//                    emit(1)
//                }
//            }

            flow.collect {
                LogUtil.e("flow call result --->", "$it")

                cancel()
            }
        }

        lifecycleScope.launch {
            HttpClient.http<ApiService, UserInfoBean> { getUserInfo() }
        }

    }

    /**
     * 模拟网络请求
     */
    private fun requestApi(block: (Int) -> Unit) {
        thread {
            Thread.sleep(3000)
            block(3)
        }
    }

}