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

    val port = 6669
    private var socketResult: ZMQ.Socket? = null
    private var socketClient: ZMQ.Socket? = null
    private var number: Int = 0
    private var tcp = ""
    private val mScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(Dispatchers.IO)
    }

    private val mServiceBuffer: StringBuffer = StringBuffer()
    private val mClientBuffer: StringBuffer = StringBuffer()

    interface ResultCallBackListener {
        fun onCall(send: String, result: String)
    }

    private var resultListener: ResultCallBackListener? = null
    fun setResultCallBackListener(listener: ResultCallBackListener?) {
        this.resultListener = listener
    }

    private var mResultSendData = ""
    private var mResultResultData = ""
    private var mResultFlag = false

    /**
     * 接收端代码
     */
    fun initResultZmq(ipAddress: String) {
        mServiceBuffer.setLength(0)
        mResultSendData = ""
        mResultResultData = ""

        initZContext()
        log("初始化服务端---> :  $ipAddress")

        mScope.launch {
            try {
                // Socket to talk to clients
                var bind: Boolean? = false
                if (!mResultFlag) {
                    socketResult = mContext?.createSocket(SocketType.PAIR)
                    log("创建 socketService !")
                    tcp = ipAddress
                    bind = socketResult?.bind(ipAddress)
                }
                log("服务端初始化成功 $bind")
                mResultSendData = mServiceBuffer.toString()
                resultListener?.onCall(mResultSendData, mResultResultData)

                while (!Thread.currentThread().isInterrupted) {
                    // Block until a message is received
                    val reply: ByteArray = socketResult!!.recv(0)
                    mResultFlag = true
                    // Print the message
                    val content = String(reply, ZMQ.CHARSET)
                    mResultResultData = "接收端-->接收：$content"
                }
            } catch (e: ZMQException) {
                log("初始化服务端异常--->$e")
                mResultSendData = mServiceBuffer.toString()
                resultListener?.onCall(mResultSendData, mResultResultData)
                e.printStackTrace()
                mResultFlag = false
            }
        }
    }

    suspend fun sendResult() {
        try {
            if (mResultFlag) {
                val msg = "接收端-->发送--> $number"
                socketResult?.send(msg.toByteArray(ZMQ.CHARSET), 0)
                number++
                mResultSendData = msg
                resultListener?.onCall(mResultSendData, mResultResultData)
            } else {
                mResultSendData = "发送端还没有connect成功，请等待！"
                resultListener?.onCall(mResultSendData, mResultResultData)
            }
        } catch (e: ZMQException) {
            mResultSendData = "接收端发送一场：$e"
            resultListener?.onCall(mResultSendData, mResultResultData)
        }
    }

    /**
     * 发送端connect成功之后，接收端才可以发送
     */
    private var mSendFlag = false
    private var mSendSendData = ""
    private var mSendResultData = ""

    interface SendCallBackListener {
        fun onCall(send: String, result: String)
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
        runCatching {
            if (socketResult != null) {
                socketResult?.unbind(tcp)
                socketResult?.close()
                socketResult = null
            }
        }

        runCatching {
            if (socketClient != null) {
                socketClient?.disconnect(tcp)
                socketClient?.close()
                socketClient = null
            }
        }

        runCatching {
            if (mContext != null) {
                mContext?.close()
                mContext = null
            }
        }
        log("释放了zmq!")
    }
}