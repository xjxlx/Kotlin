package com.xjx.kotlin.utils.zmq.big

import android.text.TextUtils
import com.android.common.utils.LogUtil
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
    private var mJob: Job? = null
    private var mContext: ZContext? = null
    private var mSocketClient: ZMQ.Socket? = null
    private var mLoopFlag: AtomicBoolean = AtomicBoolean()
    private var mReceiverFlag: AtomicBoolean = AtomicBoolean()
    private var mTraceInfo = ""
    private var mTraceOldMsg = ""
    private var mNumber = 0
    private var mIp = ""
    private var mTraceListener: ZmqCallBackListener? = null
    private var mSendListener: ZmqCallBackListener? = null
    private var mReceiverListener: ZmqCallBackListener? = null

    private fun initZContext() {
        if (mContext == null) {
            mContext = ZContext(1)
            trace("create context !")
        }
    }

    /**
     * 发送端代码
     */
    fun initClientZmq(tcpAddress: String) {
        mTraceInfo = ""
        mLoopFlag.set(false)
        trace("initClientZmq !")
        trace("tcp:[ $tcpAddress ]")
        initZContext()

        mJob = mScope.launch {
            try {
                mSocketClient = mContext?.createSocket(SocketType.PUSH)
                trace("create socket success!")
                mSocketClient?.let { socket ->
                    socket.sendTimeOut = 3000
                    try {
                        val connected = socket.bind(tcpAddress)
                        trace("bind success : $connected")
                    } catch (it: Throwable) {
                        it.printStackTrace()
                        trace("bind failure:$it")
                    }

                    mScope.launch {
                        while (!Thread.currentThread().isInterrupted && !mLoopFlag.get()) {
                            try {
                                val receiver = socket.recv(0)
                                if (!mReceiverFlag.get()) {
                                    trace("【 receiver connect success ！】")
                                }
                                mReceiverFlag.set(true)
                                if (receiver != null) {
                                    val content = String(receiver, ZMQ.CHARSET)
                                    mReceiverListener?.onCallBack(content)
                                }
                            } catch (it: Throwable) {
                                it.printStackTrace()
                                trace("receiver connect failure :$it")
                                mReceiverFlag.set(false)
                            }
                        }
                    }
                }
            } catch (it: Throwable) {
                it.printStackTrace()
                trace("init socket failure :$it")
            }
        }
    }

    fun send(): Boolean {
        var result = false
        val response = "发送端-->发送-->：(${mNumber})"
        try {
//            if (mReceiverFlag.get()) {
            val send = mSocketClient?.send(response.toByteArray(ZMQ.CHARSET), 0)
            if (send != null) {
                result = send
            }
            mNumber++
            mSendListener?.onCallBack(response)
//            } else {
//                trace("connect address is failure ,cant send!")
//            }
        } catch (it: Throwable) {
            result = false
            it.printStackTrace()
            trace("send message failure: : $it")
        }
        // trace("send result :$result response:$response")
        return result
    }

    fun stop() {
        mReceiverFlag.set(false)
        mLoopFlag.set(true)
        try {
            if (mSocketClient != null) {
                mSocketClient?.unbind(mIp)
                mSocketClient?.close()
                mSocketClient = null
            }
        } catch (it: Throwable) {
            trace("stop socket error:$it")
        }
        mJob?.cancel()
        trace("stop zmq!")
    }

    fun release() {
        stop()
        try {
            if (mContext != null) {
                mContext?.close()
                mContext = null
            }
        } catch (it: Throwable) {
            trace("release zmq failure: $it")
        }
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
        if (!TextUtils.equals(mTraceOldMsg, content)) {
            mTraceInfo += (content + "\n\n")
            mTraceListener?.onCallBack(mTraceInfo)
            mTraceOldMsg = content
        }
        LogUtil.e(ZmqUtil.TAG, content)
    }
}