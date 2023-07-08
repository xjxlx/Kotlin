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

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        ZmqUtil6.setSendCallBackListener(object : ZmqUtil6.SendCallBackListener {
            override fun onCall(content: String?) {
                mBinding.tvData.post {
                    mBinding.tvData.text = content
                }
            }
        })

        mBinding.btnServiceBind.setOnClickListener {
            val ip = mBinding.etIp.text
            val tcp = "tcp://$ip:${ZmqUtil6.port}"
            if (TextUtils.isEmpty(ip)) {
                ToastUtil.show("ip 不能为空！")
                return@setOnClickListener
            }

//            lifecycleScope.launch(Dispatchers.IO) {
//                try {
//                    val zContext = ZContext(1)
//                    log("send ---> context!")
//                    val socket = zContext.createSocket(SocketType.PAIR)
//                    log("send ---> socket!")
//                    val connect = socket.connect(tcp)
//                    log("send ---> connect : $connect")
//
//                    while (!Thread.currentThread().isInterrupted) {
//                        val recv = socket.recv(0)
//                        val content = String(recv, ZMQ.CHARSET)
//                        log("send ---> 接收到服务端的数据 : $content")
//
//                        withContext(Dispatchers.Main) {
//                            mBinding.tvData.text = content
//                        }
//
//                        val msg = "我是发送端 --->$it"
//                        socket.send(msg.toByteArray(ZMQ.CHARSET), 0)
//                        log("send ---> 发送数据到服务器 : $content")
//                    }
//                } catch (e: ZMQException) {
//                    log("send ---> error: $e")
//                }
//            }
            ZmqUtil6.initSendZmq(tcp)
        }

        mBinding.btnSend.setOnClickListener {
            mJob = lifecycleScope.launch(Dispatchers.IO) {
                repeat(Int.MAX_VALUE) {
                    delay(100)
                    ZmqUtil6.send { content ->
                        mBinding.tvData.post {
                            mBinding.tvData.text = content
                        }
                    }
                }
            }
        }
    }

    fun log(content: String) {
        LogUtil.e("ZMQ", content)
    }

    override fun onDestroy() {
        super.onDestroy()
        ZmqUtil6.stop()
    }
}