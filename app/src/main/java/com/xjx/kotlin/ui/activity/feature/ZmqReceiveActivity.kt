package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.LogUtil
import com.android.apphelper2.utils.ToastUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityZmqReceiveBinding
import com.xjx.kotlin.utils.zmq.big.ZmqUtil6
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ZmqReceiveActivity : AppBaseBindingTitleActivity<ActivityZmqReceiveBinding>() {

    override fun setTitleContent(): String {
        return "Zmq接收端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityZmqReceiveBinding {
        return ActivityZmqReceiveBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        ZmqUtil6.setResultCallBackListener(object : ZmqUtil6.ResultCallBackListener {
            override fun onCall(send: String, result: String) {
                mBinding.tvDataSend.post {
                    mBinding.tvDataSend.text = "$send"
                }
                mBinding.tvDataResult.post {
                    mBinding.tvDataResult.text = "$result"
                }
            }
        })

        mBinding.btnReceiver.setOnClickListener {
            val ip = mBinding.tvIp.text
            if (TextUtils.isEmpty(ip)) {
                ToastUtil.show("Ip不能为空！")
                return@setOnClickListener
            }

            // val tcp = "tcp://$ip:${ZmqUtil6.port}"
            val tcp = "tcp://*:${ZmqUtil6.port}"
            ToastUtil.show("开始接收！")
            ZmqUtil6.initResultZmq(tcp)
        }
        mBinding.btnSend.setOnClickListener {
            lifecycleScope.launch {
                repeat(Int.MAX_VALUE) {
                    ZmqUtil6.sendResult()
                    delay(200)
                }
            }
        }
    }

    fun log(content: String) {
        LogUtil.e("ZMQ", content)
    }

    override fun onDestroy() {
        super.onDestroy()
        ZmqUtil6.stop()
    }
}