package com.xjx.kotlin.ui.activity.feature

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.NetworkUtil
import com.android.apphelper2.utils.socket.SocketListener
import com.android.apphelper2.utils.socket.SocketServerUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivitySocketResultBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SocketResultActivity : AppBaseBindingTitleActivity<ActivitySocketResultBinding>() {

    private val socketUtil = SocketServerUtil()
    private val mNetWorkUtil: NetworkUtil by lazy {
        return@lazy NetworkUtil.instance.register()
    }

    override fun setTitleContent(): String {
        return "Socket - 接收端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivitySocketResultBinding {
        return ActivitySocketResultBinding.inflate(inflater, container, true)
    }

    @SuppressLint("SetTextI18n")
    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // 追踪 + 发送
        socketUtil.setTraceListener(object : SocketListener {
            override fun callBackListener(content: String) {
                mBinding.tvSend.post {
                    mBinding.tvSend.text = content
                }
            }
        })

        socketUtil.setResultListener(object : SocketListener {
            override fun callBackListener(content: String) {
                mBinding.tvResult.post {
                    mBinding.tvResult.text = content
                }
            }
        })

        mBinding.btnInitSocket.setOnClickListener {
            socketUtil.initSocketService()

            lifecycleScope.launch {
                mNetWorkUtil.getIPAddress {
                    mBinding.tvIp.post {
                        mBinding.tvIp.text = "当前的ip:${it.ip} 当前wifi: ${it.ssid}"
                    }
                }
            }
        }

        mBinding.btnSend.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                repeat(Int.MAX_VALUE) {
                    val send = socketUtil.send("服务端-->发送：$it")
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
        socketUtil.stop()
    }
}