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
            try {
                trace("create socket !")
                socketClient = mContext?.createSocket(SocketType.PULL)
                socketClient?.let { socket ->
                    var connected = false
                    try {
                        connected = socket.connect(tcpAddress)
                        mIp = tcpAddress
                        trace("connect success!")
                    } catch (it: Throwable) {
                        connected = false
                        trace("connect failure : $it")
                        it.printStackTrace()
                    }

                    if (!connected) {
                        trace("connect failure , break!")
                        return@launch
                    }

//                    mScope.launch {
//                        try {
//                            trace("send connect success flag--->")
//                            socket.send("success".toByteArray(ZMQ.CHARSET), 0)
//                        } catch (it: Throwable) {
//                            trace("send connect failure:$it")
//                        }
//                    }

                    mScope.launch {
                        while (!Thread.currentThread().isInterrupted && !mLoopFlag.get()) {
                            try {
                                val receiver = socket.recv(0)
                                if (!mReceiverFlag.get()) {
                                    trace("【 server connect success ！】")
                                }
                                mReceiverFlag.set(true)
                                if (receiver != null) {
                                    val content = String(receiver, ZMQ.CHARSET)
                                    mReceiverListener?.onCallBack(content)
                                }
                            } catch (it: Throwable) {
                                mReceiverFlag.set(false)
                                trace("receiver msg failure: $it")
                                it.printStackTrace()
                            }
                        }
                    }
                }
            } catch (it: Throwable) {
                it.printStackTrace()
                trace("initSocket failure: $it")
            }
        }
    }

    fun send(): Boolean {
        try {
            val response = "接收端-->发送-->：(${mNumber})"
            socketClient?.send(response.toByteArray(ZMQ.CHARSET), 0)
            mNumber++
            mSendListener?.onCallBack(response)
            return true
        } catch (it: Throwable) {
            trace("send message failure: : $it")
        }
        return false
    }

    fun stop() {
        mTraceInfo = ""
        mNumber = 0
        mLoopFlag.set(true)
        mReceiverFlag.set(false)
        try {
            socketClient?.let {
                it.disconnect(mIp)
                it.close()
                mIp = ""
            }
            socketClient = null
        } catch (it: Throwable) {
            trace("stop zmq failure: $it")
        }
        mJob?.cancel()
        trace("stop zmq!")
    }

    fun release() {
        stop()
        try {
            mContext?.let {
                if (!it.isClosed) {
                    it.close()
                }
            }
            mContext = null
        } catch (it: Throwable) {
            trace("release zmq failure: $it")
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