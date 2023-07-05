package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.NetworkUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityZmqReceiveBinding
import com.xjx.kotlin.utils.zmq.receive.ZmqUtil2
import kotlinx.coroutines.launch

class ZmqReceiveActivity : AppBaseBindingTitleActivity<ActivityZmqReceiveBinding>() {

    private val mNetwork: NetworkUtil by lazy {
        return@lazy NetworkUtil.instance.register()
    }

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
            lifecycleScope.launch {
                mNetwork.getIPAddress {
                    mBinding.tvContent.text = "ip: $it"
                    ZmqUtil2.log("IP: $it")
                    ZmqUtil2.IP_ADDRESS = it
                    ZmqUtil2.start()
                }
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
        ZmqUtil2.pause()
    }
}