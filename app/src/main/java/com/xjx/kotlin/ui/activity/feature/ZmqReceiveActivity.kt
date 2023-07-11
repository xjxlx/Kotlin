package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.ToastUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityZmqReceiveBinding
import com.xjx.kotlin.utils.zmq.big.ZmqCallBackListener
import com.xjx.kotlin.utils.zmq.big.ZmqInfo
import com.xjx.kotlin.utils.zmq.big.ZmqReceiverUtil
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ZmqReceiveActivity : AppBaseBindingTitleActivity<ActivityZmqReceiveBinding>() {

    private val mZmq: ZmqReceiverUtil = ZmqReceiverUtil()

    override fun setTitleContent(): String {
        return "Zmq接收端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityZmqReceiveBinding {
        return ActivityZmqReceiveBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mZmq.setTraceListener(object : ZmqCallBackListener {
            override fun onCallBack(content: String) {
                mBinding.tvDataSend.post {
                    mBinding.tvDataSend.text = content
                }
            }
        })

        mZmq.setReceiverListener(object : ZmqCallBackListener {
            override fun onCallBack(content: String) {
                mBinding.tvDataResult.post {
                    mBinding.tvDataResult.text = content
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
            val tcp = "tcp://*:${ZmqInfo.PORT}"
            ToastUtil.show("开始接收！")
            mZmq.initReceiverZmq(tcp)
        }
        mBinding.btnSend.setOnClickListener {
            lifecycleScope.launch {
                repeat(Int.MAX_VALUE) {
                    val send = mZmq.send()
                    if (!send) {
                        cancel()
                    }
                    delay(200)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mZmq.stop()
    }
}