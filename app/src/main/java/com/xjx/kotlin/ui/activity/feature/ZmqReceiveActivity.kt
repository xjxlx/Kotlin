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
import com.xjx.kotlin.databinding.ActivityZmqReceiveBinding
import com.xjx.kotlin.utils.zmq.big.ZmqUtil6
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

        ZmqUtil6.setResultCallBackListener(object : ZmqUtil6.ResultCallBackListener {
            override fun onCall(content: String?) {
                mBinding.tvData.post {
                    mBinding.tvData.text = content
                }
            }
        })

        mBinding.btnReceiver.setOnClickListener {
            val ip = mBinding.tvIp.text
            if (TextUtils.isEmpty(ip)) {
                ToastUtil.show("Ip不能为空！")
                return@setOnClickListener
            }

            val tcp = "tcp://$ip:${ZmqUtil6.port}"

            ToastUtil.show("开始接收！")
            ZmqUtil6.initResultZmq(tcp)

//            lifecycleScope.launch(Dispatchers.IO) {
//                try {
//                    val zContext = ZContext(1)
//                    log("result ---> context!")
//                    val socket = zContext.createSocket(SocketType.PAIR)
//                    log("result ---> socket!")
//                    val connect = socket.bind(tcp)
//                    log("result ---> connect : $connect")
//
//                    while (!Thread.currentThread().isInterrupted) {
//                        val recv = socket.recv(0)
//                        val content = String(recv, ZMQ.CHARSET)
//                        log("result ---> 接收到发送端的数据 : $content")
//
//                        withContext(Dispatchers.Main) {
//                            mBinding.tvData.text = content
//                        }
//
//                        val msg = "我是服务端 --->$it"
//                        socket.send(msg.toByteArray(ZMQ.CHARSET), 0)
//                        log("result ---> 发送数据到发送端 : $content")
//                    }
//                } catch (e: ZMQException) {
//                    log("result ---> error: $e")
//                }
//            }
        }
        mBinding.btnSend.setOnClickListener {
            lifecycleScope.launch {
//                ZmqUtil6.sendResult {
//                    mBinding.tvData.post {
//                        mBinding.tvData.text = it
//                    }
//                }
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