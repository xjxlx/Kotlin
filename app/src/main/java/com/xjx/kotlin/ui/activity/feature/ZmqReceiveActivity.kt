package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.os.Message
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.HandlerUtil
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
    private val mHandler: HandlerUtil = HandlerUtil()

    override fun setTitleContent(): String {
        return "Zmq接收端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityZmqReceiveBinding {
        return ActivityZmqReceiveBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mHandler.setHandlerCallBackListener(object : HandlerUtil.HandlerMessageListener {
            override fun handleMessage(msg: Message) {
                val obj = msg.obj as String
                when (msg.what) {
                    100 -> {
                        mBinding.tvTrace.text = obj
                    }
                    101 -> {
                        mBinding.tvDataSend.text = obj
                    }
                    102 -> {
                        mBinding.tvDataResult.text = obj
                    }
                }
            }
        })

        mZmq.setTraceListener(object : ZmqCallBackListener {
            override fun onCallBack(content: String) {
                val message = mHandler.getMessage()
                message.what = 100
                message.obj = content
                mHandler.send(message)
            }
        })

        mZmq.setSendListener(object : ZmqCallBackListener {
            override fun onCallBack(content: String) {
                val message = mHandler.getMessage()
                message.what = 101
                message.obj = content
                mHandler.send(message)
            }
        })

        mZmq.setReceiverListener(object : ZmqCallBackListener {
            override fun onCallBack(content: String) {
                val message = mHandler.getMessage()
                message.what = 102
                message.obj = content
                mHandler.send(message)
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