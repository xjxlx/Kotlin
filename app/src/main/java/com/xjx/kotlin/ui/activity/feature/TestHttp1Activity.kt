package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.android.http.client.HttpClient
import com.android.http.client.RetrofitHelper
import com.android.http.listener.HttpCallBackListener
import com.android.http.test.TestApiService
import com.xjx.kotlin.databinding.ActivityTestHttp1Binding
import kotlinx.coroutines.launch

class TestHttp1Activity : BaseBindingTitleActivity<ActivityTestHttp1Binding>() {

    override fun getTitleContent(): String {
        return "测试网络连接-1"
    }

    private val mApi: TestApiService by lazy {
        RetrofitHelper.setBaseUrl("https://web.jollyeng.com/")
        return@lazy HttpClient.getApi<TestApiService>()
    }
    private val mParameters: MutableMap<String, Any> by lazy {
        return@lazy mutableMapOf<String, Any>().apply {
            val unId = "o9RWl1EJPHolk8_7smU39k1-LqVs"
            val suiJi = "newcL6_2"
            put("service", "App.App2022.GetBook")
            put("unid", unId)
            put("suiji", suiJi)
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTestHttp1Binding {
        return ActivityTestHttp1Binding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

        mBinding.btn1.setOnClickListener {
            lifecycleScope.launch {
                HttpClient.httpCoroutine({ mApi.getL6BookList3(it) }, mParameters, object : HttpCallBackListener<String>() {
                    override fun onSuccess(t: String) {
                        LogUtil.e("onSuccess：${t}")
                    }

                    override fun onFailure(exception: Throwable) {
                        super.onFailure(exception)
                        LogUtil.e("onFailure：${exception.message}")
                    }
                })
            }
        }
    }
}