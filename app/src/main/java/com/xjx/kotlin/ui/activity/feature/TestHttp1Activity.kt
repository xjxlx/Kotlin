package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.android.http.client.HttpClient
import com.android.http.listener.HttpCallBackListener
import com.android.http.test.HttpResponse
import com.android.http.test.L6HomeRightBookListBean
import com.android.http.test.TestApiService
import com.xjx.kotlin.databinding.ActivityTestHttp1Binding
import kotlinx.coroutines.launch

class TestHttp1Activity : BaseBindingTitleActivity<ActivityTestHttp1Binding>() {

    override fun getTitleContent(): String {
        return "测试网络连接-1"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTestHttp1Binding {
        return ActivityTestHttp1Binding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.btn1.setOnClickListener {
            lifecycleScope.launch {
                val unId = "o9RWl1EJPHolk8_7smU39k1-LqVs"
                val suiJi = "newcL6_2"
                val mParameters = mutableMapOf<String, Any>()
                mParameters["service"] = "App.App2022.GetBook"
                mParameters["unid"] = unId
                mParameters["suiji"] = suiJi

                HttpClient.http<TestApiService, MutableMap<String, Any>, HttpResponse<L6HomeRightBookListBean>>({ getL6BookList(it) }, mParameters, object : HttpCallBackListener<HttpResponse<L6HomeRightBookListBean>>() {
                    override fun onFailure(exception: Throwable) {
                        super.onFailure(exception)
                        LogUtil.e("sss", "error:${exception}")
                    }

                    override fun onSuccess(t: HttpResponse<L6HomeRightBookListBean>) {
                        LogUtil.e("sss", t)
                    }
                })

                HttpClient.http<TestApiService, MutableMap<String, Any>, HttpResponse<L6HomeRightBookListBean>>({ getL6BookList(it) }, mParameters, object : HttpCallBackListener<HttpResponse<L6HomeRightBookListBean>>() {
                    override fun onSuccess(t: HttpResponse<L6HomeRightBookListBean>) {
                    }
                })
            }
        }
    }

    fun sss() {
        val launch = lifecycleScope.launch {
            val api = HttpClient.getApi<TestApiService>()
            val api1 = HttpClient.getApi<TestApiService>()
        }

        val api = HttpClient.getApi<TestApiService>()

        lifecycleScope.launch {
            val mParameters = mutableMapOf<String, Any>()
            api.getL6BookList(mParameters)

//            HttpClient.http2<TestApiService, MutableMap<String, Any>, HttpResponse<L6HomeRightBookListBean>>({ getL6BookList(mParameters) }, object : HttpCallBackListener<HttpResponse<L6HomeRightBookListBean>>() {
//                override fun onSuccess(t: HttpResponse<L6HomeRightBookListBean>) {
//                }
//            })
//            HttpClient.http2<TestApiService, MutableMap<String, Any>, HttpResponse<L6HomeRightBookListBean>>({ getL6BookList(mParameters) }, object : HttpCallBackListener<HttpResponse<L6HomeRightBookListBean>>() {
//                override fun onSuccess(t: HttpResponse<L6HomeRightBookListBean>) {
//                }
//            })

        }
    }
}