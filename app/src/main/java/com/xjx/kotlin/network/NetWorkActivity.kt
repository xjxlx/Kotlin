package com.xjx.kotlin.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityNetWorkBinding
import com.xjx.kotlin.network.ApiLogic.UserLogic
import com.xjx.kotlin.network.bean.UserInfoBean
import com.xjx.kotlin.network.listener.HttpCallBackListener
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

    }

}