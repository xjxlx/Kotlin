package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityZmqSocketBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.zeromq.SocketType
import org.zeromq.ZMQ
import java.io.PrintStream
import java.net.ServerSocket
import java.net.Socket

class ZmqSocketActivity : AppBaseBindingTitleActivity<ActivityZmqSocketBinding>() {

    private val mZmqPort: Int = 9999
    private val mSocketPort: Int = 9998
    private val tag = "ZMQ"
    private var mSocket: Socket? = null
    private val encoding = "UTF-8"
    private var mStream: PrintStream? = null
    private val tcp: String by lazy {
        return@lazy "tcp://${"192.168.8.85"}:$mZmqPort"
    }

    override fun setTitleContent(): String {
        return "ZMQ - Socket"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityZmqSocketBinding {
        return ActivityZmqSocketBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.btnStart.setOnClickListener {
            initSocket()
            initZmq()
        }
    }

    private fun initZmq() {
        lifecycleScope.launch(Dispatchers.IO) {
            runCatching {
                log("init - ZMQ !")
                val context = ZMQ.context(1)
                log("ZMQ - context create success !")
                val socket = context.socket(SocketType.PAIR)
                log("ZMQ - socket create success !")
                val bind = socket.bind(tcp)
                log("ZMQ - bind address success : $bind")
                if (context != null) {
                    while (!context.isTerminated) {
                        val bytes = socket.recv(0)
                        if (bytes != null) {
                            val content = bytes.toString(Charsets.UTF_8)
                            sendSocket(content)
                        } else {
                            log("receiver is null !")
                        }
                    }
                }
            }.onFailure {
                log("zmq error: " + it.message)
            }
        }
    }

    private fun initSocket() {
        lifecycleScope.launch {
            runCatching {
                val serverSocket = ServerSocket(mSocketPort)
                log("create socket success !")
                while (true) {
                    // Starts blocking the thread, waiting for the client to connect
                    log("start blocking the thread , waiting for the client to connect  !")
                    mSocket = serverSocket.accept()
                    val inetAddress = mSocket?.inetAddress
                    log("client connect success: host address: ：${inetAddress?.hostAddress}  host name:${inetAddress?.hostName}")
                }
            }.onFailure {
                log("socket error: " + it.message)
            }
        }
    }

    private suspend fun sendSocket(content: String) {
        if (mSocket == null) {
            log("socket is null !")
            return
        }
        mSocket?.let {
            val connected = it.isConnected
            if (connected) {
                if (mStream == null) {
                    mStream = PrintStream(it.getOutputStream(), true, encoding)
                }
                mStream?.let { stream ->
                    log("send ---> $content")
                    withContext(Dispatchers.Main) {
                        mBinding.tvContent.text = content
                    }
                    stream.println(content)
                }
            } else {
                log("socket is disConnect!")
            }
        }
    }

    private fun log(content: String) {
        LogUtil.e(tag, content)
    }
}