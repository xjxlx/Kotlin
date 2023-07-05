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
import com.xjx.kotlin.databinding.ActivityZmqSenderBinding
import com.xjx.kotlin.utils.zmq.TCP
import com.xjx.kotlin.utils.zmq.send.ZmqSendUtil
import kotlinx.coroutines.*

class ZmqSenderActivity : AppBaseBindingTitleActivity<ActivityZmqSenderBinding>() {

    private val mZmqSendUtil: ZmqSendUtil by lazy {
        return@lazy ZmqSendUtil()
    }
    private var mJob: Job? = null

    override fun setTitleContent(): String {
        return "ZMQ 发送端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityZmqSenderBinding {
        return ActivityZmqSenderBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mBinding.btnStart.setOnClickListener {

            val ip = mBinding.etIp.text.toString()
            if (TextUtils.isEmpty(ip)) {
                ToastUtil.show("Ip地址为空！")
                return@setOnClickListener
            }
            TCP.ip_address = ip

            LogUtil.e("ZMQ", "IP:${TCP.TCP_ADDRESS}")

            mZmqSendUtil.connect(TCP.TCP_ADDRESS) {
                if (it) {
                    mBinding.etIp.setText("ip: ${TCP.TCP_ADDRESS}")
                    mBinding.etIp.isEnabled = false
                }
                lifecycleScope.launch {
                    // ZmqUtil2.log("绑定成功： $it")
                    ToastUtil.show("绑定成功：$it")
                }
            }
        }

        mBinding.btnSend.setOnClickListener {
            mJob = lifecycleScope.launch {
                repeat(Int.MAX_VALUE) {
                    delay(500)
                    val data = "当前的count: $it"
                    mZmqSendUtil.send(data)
                    withContext(Dispatchers.Main) {
                        mBinding.tvData.text = data
                    }
                }
            }
        }
        mBinding.btnPause.setOnClickListener {
            mJob?.cancel()
        }
    }
}