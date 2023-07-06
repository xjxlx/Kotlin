package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityZmqSenderBinding
import com.xjx.kotlin.utils.zmq.big.ZmqUtil6
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ZmqSenderActivity : AppBaseBindingTitleActivity<ActivityZmqSenderBinding>() {

    private var mJob: Job? = null

    override fun setTitleContent(): String {
        return "ZMQ 发送端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityZmqSenderBinding {
        return ActivityZmqSenderBinding.inflate(inflater, container, true)
    }

    private var zmq6: ZmqUtil6 = ZmqUtil6()

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mBinding.btnServiceBind.setOnClickListener {
            zmq6.initTcpService()
        }

        mBinding.btnSend.setOnClickListener {
            mJob = lifecycleScope.launch(Dispatchers.IO) {
                repeat(Int.MAX_VALUE) {
                    delay(100)
                    zmq6.sendService {
                        mBinding.tvData.post {
                            mBinding.tvData.text = it
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        zmq6.stop()
        super.onDestroy()
    }
}