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
    private var socketService: ZMQ.Socket? = null
    private var clientService: ZMQ.Socket? = null
    private var number: Int = 0
    private val mScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(Dispatchers.IO)
    }

    private val mServiceBuffer: StringBuffer = StringBuffer()
    private val mClientBuffer: StringBuffer = StringBuffer()
    private var mBind: Boolean = false

    /**
     * 初始化发送端代码
     */
    fun initServiceZmq(ipAddress: String) {
        mServiceBuffer.setLength(0)

        initZContext()
        log("初始化服务端---> :  $ipAddress")

        mScope.launch {
            try {
                // Socket to talk to clients
                socketService = mContext?.createSocket(SocketType.PAIR)
                log("创建 socketService !")
                val bind = socketService?.bind(ipAddress)
                if (bind != null) {
                    mBind = bind
                }
                log("服务端初始化成功!")

                serviceListener?.onCall(mServiceBuffer.toString())

                while (!Thread.currentThread().isInterrupted) {
                    // Block until a message is received
                    val reply: ByteArray = socketService!!.recv(0)
                    // Print the message
                    log("Received: [" + String(reply, ZMQ.CHARSET) + "]")
                    // Send a response
                    val response = "Hello, world!"
                    socketService!!.send(response.toByteArray(ZMQ.CHARSET), 0)
                }
            } catch (e: ZMQException) {
                log("初始化服务端异常--->$e")
                serviceListener?.onCall(mServiceBuffer.toString())
                e.printStackTrace()
            }
        }
    }

    suspend fun sendService(block: (String) -> Unit) {
        val response = "服务端--->：($number)"
        log("send --->$response   bind: $mBind")
        if (mBind) {
            socketService?.send(response.toByteArray(ZMQ.CHARSET), 0)
            block(response)
            number++
        }
    }

    /**
     * 接收端代码
     */
    fun initClientZmq(tcpAddress: String) {
        mClientBuffer.setLength(0)
        initZContext()
        log("客户端连接--->$tcpAddress")

        mScope.launch {
            try {
                clientService = mContext?.createSocket(SocketType.PAIR)
                log("clientService---> ")
                clientService?.connect(tcpAddress)
                log("bind---> ")

                if (clientListener != null) {
                    clientListener!!.onCall(mClientBuffer.toString())
                }

                while (!Thread.currentThread().isInterrupted) {
                    // Block until a message is received
                    val reply = clientService?.recv(0)
                    if (reply != null) {
                        val content = String(reply, ZMQ.CHARSET)
                        log("客户端接收到服务端发送的数据---->$content")
                        if (clientListener != null) {
                            clientListener!!.onCall(content)
                        }
                    }
                }
            } catch (e: ZMQException) {
                log("客户端连接发送异常--->$e")
                e.printStackTrace()
                if (clientListener != null) {
                    clientListener!!.onCall(mClientBuffer.toString())
                }
            }
        }
    }

    interface ClientCallBackListener {
        fun onCall(content: String?)
    }

    private var clientListener: ClientCallBackListener? = null
    fun setClientCallBackListener(listener: ClientCallBackListener?) {
        this.clientListener = listener
    }

    interface ServiceCallBackListener {
        fun onCall(content: String?)
    }

    private var serviceListener: ServiceCallBackListener? = null
    fun setServiceCallBackListener(listener: ServiceCallBackListener?) {
        this.serviceListener = listener
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
        if (clientService != null) {
            clientService!!.close()
            clientService = null
        }

        if (socketService != null) {
            socketService!!.close()
            socketService = null
        }

        if (mContext != null) {
            mContext!!.close()
            mContext = null
        }
        log("释放了zmq!")
    }
}