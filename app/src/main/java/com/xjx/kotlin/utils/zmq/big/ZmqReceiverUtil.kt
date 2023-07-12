package com.xjx.kotlin.utils.zmq.big

import com.android.apphelper2.utils.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import org.zeromq.ZMQException
import java.util.concurrent.atomic.AtomicBoolean

class ZmqReceiverUtil {

    private val mScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(Dispatchers.IO)
    }
    private var mSocketReceiver: ZMQ.Socket? = null
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
     * 接收端代码
     */
    fun initReceiverZmq(tcpAddress: String) {
        mTraceInfo = ""
        mLoopFlag.set(false)
        trace("initReceiverZmq !")
        trace("tcp:[ $tcpAddress ]")
        initZContext()

        mJob = mScope.launch {
            try {
                if (mConnected) {
                    trace("socket is binding !")
                    return@launch
                }

                try {
                    trace("create socket --->")
                    mSocketReceiver = mContext?.createSocket(SocketType.PAIR)
                    trace("create socket success!")
                } catch (e: ZMQException) {
                    mConnected = false
                    trace("create socket failure : $e")
                }

                if (mSocketReceiver != null) {
                    try {
                        trace("bind --->")
                        mIp = tcpAddress
                        mConnected = mSocketReceiver!!.bind(tcpAddress)
                        trace("bind success!")

                        // loop wait client send message
                        while (!Thread.currentThread().isInterrupted && !mLoopFlag.get()) {
                            try {
                                try {
                                    val receiver = mSocketReceiver?.recv(0)
                                    if (!mReceiverFlag.get()) {
                                        trace("client bind success!")
                                    }
                                    mReceiverFlag.set(true)
                                    if (receiver != null) {
                                        val content = String(receiver, ZMQ.CHARSET)
                                        mReceiverListener?.onCallBack(content)
                                    }
                                } catch (e: ZMQException) {
                                    releaseSocket()
                                    trace("receiver failure :$e")
                                }
                            } catch (e: ZMQException) {
                                releaseSocket()
                                trace("receiver failure : $e")
                            }
                        }
                    } catch (e: ZMQException) {
                        releaseSocket()
                        trace("bind failure : $e")
                    }
                }
            } catch (e: ZMQException) {
                releaseSocket()
                trace("zmq receiver failure : $e")
                e.printStackTrace()
            }
        }
    }

    private fun releaseSocket() {
        mReceiverFlag.set(false)
        mConnected = false
    }

    fun send(): Boolean {
        try {
            val response = "接收端-->发送-->：(${mNumber})"
            try {
                if (mReceiverFlag.get()) {
                    trace("send  data: $response")
                    mSocketReceiver?.send(response.toByteArray(ZMQ.CHARSET), 0)
                    mNumber++
                    // mSendListener?.onCallBack(response)
                    return true
                } else {
                    trace("bind address is failure ,cant send!")
                }
            } catch (e: ZMQException) {
                trace("send message failure: : $e")
            }
        } catch (e: ZMQException) {
            trace("send message failure: : $e")
        }
        return false
    }

    fun stop() {
        releaseSocket()
        mLoopFlag.set(true)
        runCatching {
            if (mSocketReceiver != null) {
                mSocketReceiver?.unbind(mIp)
                mSocketReceiver?.close()
                mSocketReceiver = null
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

    fun setReceiverListener(listener: ZmqCallBackListener) {
        this.mReceiverListener = listener
    }

    fun setSendListener(listener: ZmqCallBackListener) {
        this.mSendListener = listener
    }

    private fun trace(content: String) {
        mTraceInfo += (content + "\n\n")
        mTraceListener?.onCallBack(mTraceInfo)
        LogUtil.e(ZmqUtil.TAG, content)
    }
}