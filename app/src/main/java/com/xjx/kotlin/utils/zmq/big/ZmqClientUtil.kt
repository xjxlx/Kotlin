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

class ZmqClientUtil {

    private val mScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(Dispatchers.IO)
    }
    private var socketClient: ZMQ.Socket? = null
    private var mJob: Job? = null
    private var mConnected: Boolean = false
    private var mReceiverFlag: AtomicBoolean = AtomicBoolean()
    private var mTraceInfo = ""
    private var mNumber = 0
    private var mIp = ""
    private var mTraceListener: ZmqCallBackListener? = null
    private var mSendListener: ZmqCallBackListener? = null
    private var mReceiverListener: ZmqCallBackListener? = null
    private var mContext: ZContext? = null
    private var mLoopFlag: AtomicBoolean = AtomicBoolean()

    private fun initZContext() {
        if (mContext == null) {
            mContext = ZContext(1)
            trace("create context !")
        }
    }

    /**
     * 发送端代码
     */
    fun initSendZmq(tcpAddress: String) {
        mTraceInfo = ""
        mLoopFlag.set(false)
        trace("initSendZmq !")
        trace("tcp:[ $tcpAddress ]")
        initZContext()

        if (mReceiverFlag.get()) {
            trace("connected succeeded , break!")
            return
        }

        mJob = mScope.launch {
            runCatching {
                trace("create socket !")
                socketClient = mContext?.createSocket(SocketType.PAIR)
                socketClient?.let {
                    runCatching {
                        mConnected = it.connect(tcpAddress)
                        mIp = tcpAddress
                        trace("connect success!")
                    }.onFailure {
                        trace("connect failure : $it")
                        it.printStackTrace()
                    }

                    mScope.launch {
                        trace("send connect success flag--->")
                        it.send("success".toByteArray(ZMQ.CHARSET), 0)
                    }

                    mScope.launch {
                        while (!Thread.currentThread().isInterrupted && !mLoopFlag.get()) {
                            runCatching {
                                val receiver = socketClient?.recv(0)
                                if (!mReceiverFlag.get()) {
                                    trace("【 server connect success ！】")
                                }
                                mReceiverFlag.set(true)
                                if (receiver != null) {
                                    val content = String(receiver, ZMQ.CHARSET)
                                    mReceiverListener?.onCallBack(content)
                                }
                            }.onFailure {
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
            val response = "发送端-->发送-->：(${mNumber})"
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

        runCatching {
            if (mContext != null) {
                mContext?.close()
                mContext = null
            }
        }
        mJob?.cancel()
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