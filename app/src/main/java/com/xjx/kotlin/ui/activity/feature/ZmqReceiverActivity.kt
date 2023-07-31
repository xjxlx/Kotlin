package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.os.Message
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.HandlerUtil
import com.android.apphelper2.utils.NetworkUtil
import com.android.apphelper2.utils.ToastUtil
import com.android.common.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityZmqReceiverBinding
import com.xjx.kotlin.utils.zmq.big.ZmqCallBackListener
import com.xjx.kotlin.utils.zmq.big.ZmqUtil
import kotlinx.coroutines.*

class ZmqReceiverActivity : AppBaseBindingTitleActivity<ActivityZmqReceiverBinding>() {

    private var mJob: Job? = null
    private val mHandler: HandlerUtil = HandlerUtil()
    private val mNetWorkUtil: NetworkUtil by lazy {
        return@lazy NetworkUtil.instance.register()
    }

    override fun setTitleContent(): String {
        return "ZMQ-接收端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityZmqReceiverBinding {
        return ActivityZmqReceiverBinding.inflate(inflater, container, true)
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

        ZmqUtil.setReceiverTraceListener(object : ZmqCallBackListener {
            override fun onCallBack(content: String) {
                val message = mHandler.getMessage()
                message.what = 100
                message.obj = content
                mHandler.send(message)
            }
        })

        ZmqUtil.setReceiverSendListener(object : ZmqCallBackListener {
            override fun onCallBack(content: String) {
                val message = mHandler.getMessage()
                message.what = 101
                message.obj = content
                mHandler.send(message)
            }
        })

        ZmqUtil.setReceiverReceiverListener(object : ZmqCallBackListener {
            override fun onCallBack(content: String) {
                val message = mHandler.getMessage()
                message.what = 102
                message.obj = content
                mHandler.send(message)
            }
        })

        // 查看IP
        mBinding.btnIp.setOnClickListener {
            lifecycleScope.launch {
                mNetWorkUtil.getIPAddress { ip ->
                    mBinding.etIp.setText(ip.ip)
                }
            }
        }

        // 初始化
        mBinding.btnServiceBind.setOnClickListener {
            val ip = mBinding.etIp.text
            val tcp = "tcp://$ip:${ZmqUtil.PORT}"
//            val tcp = "tcp://localhost:${ZmqUtil6.port}"
            if (TextUtils.isEmpty(ip)) {
                ToastUtil.show("ip 不能为空！")
                return@setOnClickListener
            }

            ZmqUtil.initReceiverZmq(tcp.trim())
        }

        // 关闭
        mBinding.btnServiceClose.setOnClickListener {
            ZmqUtil.releaseReceiver()
        }


        mBinding.btnSend.setOnClickListener {
            mJob = lifecycleScope.launch(Dispatchers.IO) {
                repeat(Int.MAX_VALUE) {
                    delay(100)
                    val send = ZmqUtil.sendReceiver()
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
        ZmqUtil.releaseReceiver()
    }
}