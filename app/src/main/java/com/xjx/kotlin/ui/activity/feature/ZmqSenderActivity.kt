package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.os.Message
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.HandlerUtil
import com.android.apphelper2.utils.LogUtil
import com.android.apphelper2.utils.ToastUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityZmqSenderBinding
import com.xjx.kotlin.utils.zmq.big.ZmqCallBackListener
import com.xjx.kotlin.utils.zmq.big.ZmqClientUtil
import com.xjx.kotlin.utils.zmq.big.ZmqInfo
import kotlinx.coroutines.*

class ZmqSenderActivity : AppBaseBindingTitleActivity<ActivityZmqSenderBinding>() {

    private var mJob: Job? = null
    private val mZmq: ZmqClientUtil = ZmqClientUtil()
    private val mHandler: HandlerUtil = HandlerUtil()

    override fun setTitleContent(): String {
        return "ZMQ 发送端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityZmqSenderBinding {
        return ActivityZmqSenderBinding.inflate(inflater, container, true)
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

        // 初始化
        mBinding.btnServiceBind.setOnClickListener {
            val ip = mBinding.etIp.text
            val tcp = "tcp://$ip:${ZmqInfo.PORT}"
//            val tcp = "tcp://localhost:${ZmqUtil6.port}"
            if (TextUtils.isEmpty(ip)) {
                ToastUtil.show("ip 不能为空！")
                return@setOnClickListener
            }

            mZmq.initSendZmq(tcp)
        }

        // 关闭
        mBinding.btnServiceClose.setOnClickListener {
            mZmq.stop()
        }


        mBinding.btnSend.setOnClickListener {
            mJob = lifecycleScope.launch(Dispatchers.IO) {
                repeat(Int.MAX_VALUE) {
                    delay(100)
                    val send = mZmq.send()
                    if (!send) {
                        cancel()
                    }
                }
            }
        }
    }

    fun log(content: String) {
        LogUtil.e("ZMQ", content)
    }

    override fun onDestroy() {
        super.onDestroy()
        mZmq.stop()
    }
}