package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.ToastUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityZmqReceiveBinding
import com.xjx.kotlin.utils.zmq.TCP
import com.xjx.kotlin.utils.zmq.receive.ZmqUtil2
import kotlinx.coroutines.launch

class ZmqReceiveActivity : AppBaseBindingTitleActivity<ActivityZmqReceiveBinding>() {

    override fun setTitleContent(): String {
        return "Zmq接收端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityZmqReceiveBinding {
        return ActivityZmqReceiveBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        ZmqUtil2.initLog(this)

        mBinding.btnStart.setOnClickListener {
            val ip = mBinding.tvIp.text.toString()
            if (TextUtils.isEmpty(ip)) {
                ToastUtil.show("Ip为空！")
                return@setOnClickListener
            }
            TCP.ip_address = ip

            ZmqUtil2.log("IP: ${mBinding.tvIp.text}")

            lifecycleScope.launch {
                ZmqUtil2.start()
            }
        }

        mBinding.btnBind.setOnClickListener {
            ZmqUtil2.start()
        }

        mBinding.btnUnbind.setOnClickListener {
            ZmqUtil2.pause()
        }

        ZmqUtil2.setCallBackListener {
            mBinding.tvData.post {
                mBinding.tvData.text = it
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ZmqUtil2.stop()
    }
}