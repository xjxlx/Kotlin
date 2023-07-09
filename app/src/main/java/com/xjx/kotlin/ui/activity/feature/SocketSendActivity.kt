package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.SocketUtil
import com.android.apphelper2.utils.ToastUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivitySocketSendBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SocketSendActivity : AppBaseBindingTitleActivity<ActivitySocketSendBinding>() {

    private val socketUtil = SocketUtil.SocketClient()

    override fun setTitleContent(): String {
        return "Socket - 发送端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivitySocketSendBinding {
        return ActivitySocketSendBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        socketUtil.setServiceCallBackListener(object : SocketUtil.SocketClient.ClientCallBackListener {
            override fun callBack(send: String, result: String) {
                mBinding.tvSend.post {
                    mBinding.tvSend.text = send
                }
                mBinding.tvResult.post {
                    mBinding.tvResult.text = result
                }
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

        mBinding.btnSend.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                repeat(Int.MAX_VALUE) {
                    socketUtil.sendClientData("客户端-->发送：$it")
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