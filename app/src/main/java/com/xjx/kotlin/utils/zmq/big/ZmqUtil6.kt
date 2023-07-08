package com.xjx.kotlin.utils.zmq.big

import com.android.apphelper2.utils.LogUtil.e
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import org.zeromq.ZMQException

object ZmqUtil6 {

    val port = 6667
    private var socketResult: ZMQ.Socket? = null
    private var socketClient: ZMQ.Socket? = null
    private var number: Int = 0
    private val mScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(Dispatchers.IO)
    }

    private val mServiceBuffer: StringBuffer = StringBuffer()
    private val mClientBuffer: StringBuffer = StringBuffer()

    /**
     * 接收端代码
     */
    fun initResultZmq(ipAddress: String) {
        mServiceBuffer.setLength(0)

        initZContext()
        log("初始化服务端---> :  $ipAddress")

        mScope.launch {
            try {
                // Socket to talk to clients
                var bind: Boolean? = false
                if (socketResult == null) {
                    socketResult = mContext?.createSocket(SocketType.PAIR)
                    log("创建 socketService !")
                    bind = socketResult?.bind(ipAddress)
                }
                log("服务端初始化成功 $bind")

                resultListener?.onCall(mServiceBuffer.toString())

                while (!Thread.currentThread().isInterrupted) {
                    // Block until a message is received
                    val reply: ByteArray = socketResult!!.recv(0)
                    // Print the message
                    val content = String(reply, ZMQ.CHARSET)
                    resultListener?.onCall(content)
                }
            } catch (e: ZMQException) {
                log("初始化服务端异常--->$e")
                resultListener?.onCall(mServiceBuffer.toString())
                e.printStackTrace()
            }
        }
    }

    var mBind = false

    /**
     * 发送端代码
     */
    fun initSendZmq(tcpAddress: String) {
        mClientBuffer.setLength(0)
        initZContext()
        log("客户端连接--->$tcpAddress")

        mScope.launch {
            try {
                if (socketClient == null) {
                    socketClient = mContext?.createSocket(SocketType.PAIR)
                    log("clientService---> ")
                    val connect = socketClient?.connect(tcpAddress)
                    if (connect != null) {
                        mBind = connect
                    }
                }
                log("bind---> $mBind")

                if (sendListener != null) {
                    sendListener!!.onCall(mClientBuffer.toString())
                }

                while (!Thread.currentThread().isInterrupted) {
                    // Block until a message is received
                    val reply = socketClient?.recv(0)
                    if (reply != null) {
                        val content = String(reply, ZMQ.CHARSET)
                        log("客户端接收到服务端发送的数据---->$content")
                        if (sendListener != null) {
                            sendListener!!.onCall(content)
                        }
                    }
                }
            } catch (e: ZMQException) {
                log("客户端连接发送异常--->$e")
                e.printStackTrace()
                if (sendListener != null) {
                    sendListener!!.onCall(mClientBuffer.toString())
                }
                mBind = false
            }
        }
    }

    suspend fun send(block: (String) -> Unit) {
        val response = "发送数据到接收端--->：($number)"
        log("send --->$response   bind: $mBind")
        if (mBind) {
            socketClient?.send(response.toByteArray(ZMQ.CHARSET), 0)
            block(response)
            number++
        }
    }

    interface SendCallBackListener {
        fun onCall(content: String?)
    }

    private var sendListener: SendCallBackListener? = null
    fun setSendCallBackListener(listener: SendCallBackListener?) {
        this.sendListener = listener
    }

    interface ResultCallBackListener {
        fun onCall(content: String?)
    }

    private var resultListener: ResultCallBackListener? = null
    fun setResultCallBackListener(listener: ResultCallBackListener?) {
        this.resultListener = listener
    }

    private var mContext: ZContext? = null

    fun log(content: String) {
        e("ZMQ", content)
        mServiceBuffer.append(content)
            .append("\r\n")
        mClientBuffer.append(content)
            .append("\r\n")
    }

    private fun initZContext() {
        log("创建 context ---->")
        if (mContext == null) {
            mContext = ZContext(1)
        }
    }

    fun stop() {
//        if (clientService != null) {
//            clientService!!.close()
//            clientService = null
//        }
//
//        if (socketService != null) {
//            socketService!!.close()
//            socketService = null
//        }
//
//        if (mContext != null) {
//            mContext!!.close()
//            mContext = null
//        }
        log("释放了zmq!")
    }
}