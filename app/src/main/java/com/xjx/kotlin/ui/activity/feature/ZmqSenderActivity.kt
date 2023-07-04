package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ZmqSenderActivity : AppBaseBindingTitleActivity<ActivityZmqSenderBinding>() {

    private val IP: String = "IP_ADDRESS"
    private val mZmqSendUtil: ZmqSendUtil by lazy {
        return@lazy ZmqSendUtil()
    }

    override fun setTitleContent(): String {
        return "ZMQ 发送端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityZmqSenderBinding {
        return ActivityZmqSenderBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mBinding.btnStart.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) {
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
                    return@launch
                }
                mZmqSendUtil.connect(ip) {
                    if (it) {
                        mBinding.etIp.isEnabled = false
                    }
                    ZmqUtil2.log("绑定成功： $it")
                }
            }
        }

        mBinding.etInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                mZmqSendUtil.send(s.toString())
            }
        })
    }
}