package com.xjx.kotlin.utils.zmq.big

import com.android.apphelper2.utils.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import java.util.concurrent.atomic.AtomicBoolean

class ZmqReceiverUtil {

    private val mScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(Dispatchers.IO)
    }
    private var mContext: ZContext? = null
    private var socketClient: ZMQ.Socket? = null
    private var mJob: Job? = null
    private var mTraceInfo = ""
    private var mNumber = 0
    private var mIp = ""
    private var mTraceListener: ZmqCallBackListener? = null
    private var mSendListener: ZmqCallBackListener? = null
    private var mReceiverListener: ZmqCallBackListener? = null
    private var mReceiverFlag: AtomicBoolean = AtomicBoolean()
    private var mLoopFlag: AtomicBoolean = AtomicBoolean()

    private fun initZContext() {
        if (mContext == null) {
            mContext = ZContext(1)
            trace("create context !")
        }
    }

    /**
     * 接收端代码
     */
    fun initReceiverZmq(tcpAddress: String) {
        mTraceInfo = ""
        mLoopFlag.set(false)
        trace("initReceiverZmq !")
        trace("tcp:[ $tcpAddress ]")
        initZContext()

        mJob = mScope.launch {
            runCatching {
                trace("create socket !")
                socketClient = mContext?.createSocket(SocketType.PAIR)
                socketClient?.let { socket ->
                    var connected = false
                    runCatching {
                        connected = socket.connect(tcpAddress)
                        mIp = tcpAddress
                        trace("connect success!")
                    }.onFailure { connect ->
                        connected = false
                        trace("connect failure : $connect")
                        connect.printStackTrace()
                    }

                    if (!connected) {
                        trace("connect failure , break!")
                        return@launch
                    }

                    mScope.launch {
                        runCatching {
                            trace("send connect success flag--->")
                            socket.send("success".toByteArray(ZMQ.CHARSET), 0)
                        }.onFailure {
                            trace("send connect failure:$it")
                        }
                    }

                    mScope.launch {
                        while (!Thread.currentThread().isInterrupted && !mLoopFlag.get()) {
                            runCatching {
                                val receiver = socket.recv(0)
                                if (!mReceiverFlag.get()) {
                                    trace("【 server connect success ！】")
                                }
                                mReceiverFlag.set(true)
                                if (receiver != null) {
                                    val content = String(receiver, ZMQ.CHARSET)
                                    mReceiverListener?.onCallBack(content)
                                }
                            }.onFailure {
                                mReceiverFlag.set(false)
                                trace("receiver msg failure: $it")
                                it.printStackTrace()
                            }
                        }
                    }
                }
            }.onFailure {
                it.printStackTrace()
                trace("initSocket failure: $it")
            }
        }
    }

    fun send(): Boolean {
        runCatching {
            val response = "接收端-->发送-->：(${mNumber})"
            socketClient?.send(response.toByteArray(ZMQ.CHARSET), 0)
            mNumber++
            mSendListener?.onCallBack(response)
            return true
        }.onFailure {
            trace("send message failure: : $it")
        }
        return false
    }

    fun stop() {
        mLoopFlag.set(true)
        runCatching {
            if (socketClient != null) {
                socketClient?.disconnect(mIp)
                socketClient?.close()
                socketClient = null
            }
        }
        mJob?.cancel()
        trace("stop zmq!")
    }

    fun release() {
        stop()
        runCatching {
            if (mContext != null) {
                mContext?.close()
                mContext = null
            }
        }
        trace("release zmq!")
    }

    fun setTraceListener(listener: ZmqCallBackListener) {
        this.mTraceListener = listener
    }

    fun setSendListener(listener: ZmqCallBackListener) {
        this.mSendListener = listener
    }

    fun setReceiverListener(listener: ZmqCallBackListener) {
        this.mReceiverListener = listener
    }

    private fun trace(content: String) {
        mTraceInfo += (content + "\n\n")
        mTraceListener?.onCallBack(mTraceInfo)
        LogUtil.e(ZmqUtil.TAG, content)
    }
}