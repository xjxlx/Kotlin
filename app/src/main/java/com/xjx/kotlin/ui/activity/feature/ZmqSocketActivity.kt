package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityZmqSocketBinding
import com.xjx.kotlin.utils.zmq.TCP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.zeromq.SocketType
import org.zeromq.ZMQ
import java.io.PrintStream
import java.net.ServerSocket
import java.net.Socket

/**
 * 使用说明：
 * 1：先开启 ZmqSocket 页面，进行数据的等待。。。
 * 2：开始发送端...
 * 3: 开启接收端...
 */
class ZmqSocketActivity : AppBaseBindingTitleActivity<ActivityZmqSocketBinding>() {

    private val tag = "ZMQ"
    private val encoding = "UTF-8"
    private var mStream: PrintStream? = null
    private val mBuffer: StringBuffer = StringBuffer()
    private var mContext: ZMQ.Context? = null
    private var mZmqSocket: ZMQ.Socket? = null
    private var mServerSocket: ServerSocket? = null
    private var mSocket: Socket? = null

    override fun setTitleContent(): String {
        return "ZMQ - Socket"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityZmqSocketBinding {
        return ActivityZmqSocketBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.btnStart.setOnClickListener {
            lifecycleScope.launch {
                val job = lifecycleScope.launch(Dispatchers.Default) {
                    initSocket()
                }
                job.join()
                initZmq()
            }
        }
    }

    private fun initZmq() {
        lifecycleScope.launch(Dispatchers.Default) {
            runCatching {
                log("ZMQ - init --->")
                mContext = ZMQ.context(1)
                log("ZMQ - context create success !")
                mZmqSocket = mContext?.socket(SocketType.PAIR)
                log("ZMQ - ip: ${TCP.TCP_ADDRESS}")
                log("ZMQ - socket create success !")
                val bind = mZmqSocket?.bind(TCP.TCP_ADDRESS)
                log("ZMQ - bind address success : $bind")
                if (mContext != null) {
                    while (!mContext!!.isTerminated) {
                        val bytes = mZmqSocket?.recv(0)
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
        lifecycleScope.launch(Dispatchers.IO) {
            runCatching {
                mServerSocket = ServerSocket(TCP.SocketPort)
                log("create socket success , port: ${TCP.SocketPort}")
                while (true) {
                    // Starts blocking the thread, waiting for the client to connect
                    log("start blocking the thread , waiting for the client to connect  .......")
                    mSocket = mServerSocket?.accept()
                    mSocket?.let { socket ->
                        val inetAddress = socket.inetAddress
                        if (inetAddress != null) {
                            log("client connect success: host address: ：${inetAddress.hostAddress}  host name:${inetAddress.hostName}")
                        }
                    }
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
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                mBuffer.append(content)
                    .append("\r\n")
                mBinding.tvContent.text = mBuffer.toString()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mZmqSocket?.unbind(TCP.TCP_ADDRESS)
        mZmqSocket?.close()
        mZmqSocket = null
        mContext?.close()
        mContext = null

        mSocket?.close()
        mSocket = null
        mServerSocket?.close()
        mServerSocket = null
    }
}