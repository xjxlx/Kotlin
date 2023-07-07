package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.ToastUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityZmqSenderBinding
import com.xjx.kotlin.utils.zmq.big.ZmqUtil6
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ZmqSenderActivity : AppBaseBindingTitleActivity<ActivityZmqSenderBinding>() {

    private var mJob: Job? = null

    override fun setTitleContent(): String {
        return "ZMQ 发送端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityZmqSenderBinding {
        return ActivityZmqSenderBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mBinding.btnServiceBind.setOnClickListener {
            val ip = mBinding.etIp.text
            val tcp = "tcp://$ip:${ZmqUtil6.port}"
            if (TextUtils.isEmpty(ip)) {
                ToastUtil.show("ip 不能为空！")
                return@setOnClickListener
            }

            ZmqUtil6.initServiceZmq(tcp)
        }

        mBinding.btnSend.setOnClickListener {
            mJob = lifecycleScope.launch(Dispatchers.IO) {
                repeat(Int.MAX_VALUE) {
                    delay(100)
                    ZmqUtil6.log("---> $it")
                    ZmqUtil6.sendService { content ->
                        mBinding.tvData.post {
                            mBinding.tvData.text = content
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ZmqUtil6.stop()
    }
}