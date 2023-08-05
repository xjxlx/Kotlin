package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.os.Message
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.HandlerUtil
import com.android.apphelper2.utils.socket.SocketClientUtil
import com.android.apphelper2.utils.socket.SocketListener
import com.android.common.utils.ToastUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivitySocketSendBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SocketSendActivity : AppBaseBindingTitleActivity<ActivitySocketSendBinding>() {

    private val socketUtil = SocketClientUtil()
    private val mHandler: HandlerUtil by lazy {
        return@lazy HandlerUtil()
    }

    override fun setTitleContent(): String {
        return "Socket - 发送端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivitySocketSendBinding {
        return ActivitySocketSendBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mHandler.setHandlerCallBackListener(object : HandlerUtil.HandlerMessageListener {
            override fun handleMessage(msg: Message) {
                val obj = msg.obj as String
                if (msg.what == 100) {
                    mBinding.tvSend.text = obj
                } else if (msg.what == 101) {
                    mBinding.tvResult.text = obj
                }
            }
        })

        socketUtil.autoRestart()
        socketUtil.setTraceListener(object : SocketListener {
            override fun callBackListener(content: String) {
                val message = mHandler.getMessage()
                message.what = 100
                message.obj = content
                mHandler.send(message)
            }
        })

        socketUtil.setResultListener(object : SocketListener {
            override fun callBackListener(content: String) {
                val message = mHandler.getMessage()
                message.what = 101
                message.obj = content
                mHandler.send(message)
            }
        })

        mBinding.btnInitSocket.setOnClickListener {
            val ip = mBinding.etIp.text.toString()
            if (TextUtils.isEmpty(ip)) {
                ToastUtil.show("Ip不能为空！")
                return@setOnClickListener
            }
            socketUtil.initClientSocket(ip)
        }

        mBinding.btnClose.setOnClickListener {
            socketUtil.stop()
        }

        mBinding.btnSend.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                repeat(Int.MAX_VALUE) {
                    val clientData = socketUtil.send("客户端-->发送：$it")
                    if (!clientData) {
                        cancel()
                    }
                    delay(200)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socketUtil.stop()
    }
}