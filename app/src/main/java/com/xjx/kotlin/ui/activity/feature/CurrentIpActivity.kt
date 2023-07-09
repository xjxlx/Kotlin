package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.LogUtil
import com.android.apphelper2.utils.NetworkUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCurrentIpBinding
import kotlinx.coroutines.launch

class CurrentIpActivity : AppBaseBindingTitleActivity<ActivityCurrentIpBinding>() {

    override fun setTitleContent(): String {
        return "当前的ip"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCurrentIpBinding {
        return ActivityCurrentIpBinding.inflate(inflater, container, true)
    }

    private val network by lazy {
        return@lazy NetworkUtil.instance.register()
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.btnIp.setOnClickListener {
            lifecycleScope.launch {
                val ipAddress = com.android.helper.utils.NetworkUtil.getIPAddress(this@CurrentIpActivity)
                LogUtil.e("ip:1" + ipAddress)

                network.getIPAddress {
                    LogUtil.e("ip:$it")
                    mBinding.tvIp.text = it
                }
            }
        }
    }


}