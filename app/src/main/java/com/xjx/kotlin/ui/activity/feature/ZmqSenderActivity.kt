package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.SpUtil
import com.android.apphelper2.utils.ToastUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityZmqSenderBinding
import com.xjx.kotlin.utils.zmq.receive.ZmqUtil2
import com.xjx.kotlin.utils.zmq.send.ZmqSendUtil
import kotlinx.coroutines.*

class ZmqSenderActivity : AppBaseBindingTitleActivity<ActivityZmqSenderBinding>() {

    private val IP: String = "IP_ADDRESS"
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
            var ip = mBinding.etIp.text.toString()
            if (!TextUtils.isEmpty(ip)) {
                SpUtil.putString(IP, ip)
            }
            if (TextUtils.isEmpty(ip)) {
                ip = SpUtil.getString(IP)
            }
            if (TextUtils.isEmpty(ip)) {
                ToastUtil.show("IP 为空！")
                ZmqUtil2.log("发送端IP 为空！")
                return@setOnClickListener
            }

            mZmqSendUtil.connect(ip) {
                if (it) {
                    mBinding.etIp.isEnabled = false
                }
                lifecycleScope.launch {
                    ZmqUtil2.log("绑定成功： $it")
                    ToastUtil.show("绑定成功：$it")
                }
            }
        }

        mBinding.btnSend.setOnClickListener {
            mJob = lifecycleScope.launch {
                repeat(1000) {
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