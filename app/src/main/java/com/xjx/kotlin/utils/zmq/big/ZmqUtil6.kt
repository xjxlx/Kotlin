package com.xjx.kotlin.utils.zmq.big

import com.android.apphelper2.utils.LogUtil.e
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import java.util.*

class ZmqUtil6 {

    private val tcp = "tcp://192.168.8.204:6666"
    private var mContext: ZContext? = null
    private var socketService: ZMQ.Socket? = null
    private var clientService: ZMQ.Socket? = null
    private fun initZContext() {
        if (mContext == null) {
            mContext = ZContext(1)
            log("创建 context !")
        }
    }

    /**
     * 初始化发送端代码
     */
    fun initTcpService() {
        initZContext()
        log("初始化服务端---> tcp:$tcp")
        Thread {
            try {
                // Socket to talk to clients
                socketService = mContext?.createSocket(SocketType.PAIR)
                log("创建 socketService !")
                socketService?.bind(tcp)
//                socketService?.connect(tcp)
                log("服务端初始化成功!")
            } catch (e: Exception) {
                log("初始化服务端异常--->" + e.message)
                e.printStackTrace()
            }
        }.start()
    }

    fun sendService(block: (String) -> Unit) {
        val response = "服务端的消息(" + UUID.randomUUID() + ")"
        log("send --->$response")
        socketService?.send(response.toByteArray(ZMQ.CHARSET), 0)
        block(response)
    }

    /**
     * 接收端代码
     */
    fun initClient() {
        initZContext()
        log("客户端连接--->$tcp")
        Thread {
            try {
                clientService = mContext?.createSocket(SocketType.PAIR)
                log("clientService---> ")
                clientService?.connect(tcp)
//                clientService?.bind(tcp)
                log("connect---> ")

                while (!Thread.currentThread().isInterrupted) {
                    // Block until a message is received
                    val reply = clientService?.recv(0)
                    if (reply != null) {
                        val content = String(reply, ZMQ.CHARSET)
                        log("客户端接收到服务端发送的数据---->$content")
                        if (listener != null) {
                            listener!!.onCall(content)
                        }
                    }
                }
            } catch (e: Exception) {
                log("客户端连接发送异常--->" + e.message)
                e.printStackTrace()
            }
        }.start()
    }

    interface CallBackListener {
        fun onCall(content: String?)
    }

    private var listener: CallBackListener? = null
    fun setCallBackListener(listener: CallBackListener?) {
        this.listener = listener
    }

    private fun log(content: String) {
        e("ZMQ", content)
    }

    fun stop() {
        if (clientService != null) {
//            clientService!!.unbind(tcp)
//            clientService!!.disconnect(tcp)
            clientService!!.close()
            clientService = null
        }

        if (socketService != null) {
            socketService!!.unbind(tcp)
            socketService!!.disconnect(tcp)
            socketService!!.close()
            socketService = null
        }

        if (mContext != null) {
            mContext!!.close()
            mContext = null
        }
    }
}