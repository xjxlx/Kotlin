package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityZmqReceiveBinding
import com.xjx.kotlin.utils.zmq.TCP
import com.xjx.kotlin.utils.zmq.receive.ZmqUtil2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket

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
            initSocket()

//            val ip = mBinding.tvIp.text.toString()
//            if (TextUtils.isEmpty(ip)) {
//                ToastUtil.show("Ip为空！")
//                return@setOnClickListener
//            }
//            TCP.ip_address = ip
//
//            ZmqUtil2.log("IP: ${mBinding.tvIp.text}")
//
//            lifecycleScope.launch {
//                ZmqUtil2.start()
//            }
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

    // 客户端的读取数据流
    private var mClientBufferedReader: BufferedReader? = null
    private val encoding = "UTF-8"

    private fun initSocket() {
        lifecycleScope.launch(Dispatchers.IO) {
            runCatching {
                val mClientSocket = Socket(TCP.ip_address, TCP.SocketPort)

                // 取得输入流读取客户端传送的数据,要接收中文只需将编码设置为"UTF-8"
                val connected: Boolean = mClientSocket.isConnected
                if (connected) {
                    mClientBufferedReader = BufferedReader(InputStreamReader(mClientSocket.getInputStream(), encoding))
                    LogUtil.e("ZMQ", "开始循环获取数据！")

                    mClientBufferedReader?.let { reader ->
                        while (reader.readLine() != null) {
                            val readLine = reader.readLine()
                            LogUtil.e("ZMQ", "info: $readLine")
                            mBinding.tvData.post {
                                mBinding.tvData.text = readLine
                            }
                        }
                    }
                } else {
                    LogUtil.e("ZMQ", "服务端Socket链接断开！")
                }
            }.onFailure {
                LogUtil.e("ZMQ", "读取服务器数据异常：" + it.message)
                if (mClientBufferedReader != null) {
                    try {
                        mClientBufferedReader!!.close()
                        mClientBufferedReader = null
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                    }
                }
            }
        }
    }
}