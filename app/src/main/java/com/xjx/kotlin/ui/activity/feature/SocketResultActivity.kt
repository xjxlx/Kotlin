package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.SocketUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivitySocketResultBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SocketResultActivity : AppBaseBindingTitleActivity<ActivitySocketResultBinding>() {
    private val socketUtil = SocketUtil.SocketService()

    override fun setTitleContent(): String {
        return "Socket - 接收端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivitySocketResultBinding {
        return ActivitySocketResultBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        socketUtil.setServiceCallBackListener(object : SocketUtil.SocketService.ServerCallBackListener {
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
            socketUtil.initSocketService()
        }


        mBinding.btnSend.setOnClickListener {
            lifecycleScope.launch {
                repeat(Int.MAX_VALUE) {
                    socketUtil.sendServerData("服务端-->发送：$it")
                    delay(200)
                }
            }
        }
    }
}