package com.xjx.kotlin.utils.zmq.big

import android.text.TextUtils
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
    private var mJob: Job? = null
    private var mContext: ZContext? = null
    private var mSocketReceiver: ZMQ.Socket? = null
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
     * 接收端代码
     */
    fun initReceiverZmq(tcpAddress: String) {
        mTraceInfo = ""
        mLoopFlag.set(false)
        trace("initReceiverZmq !")
        trace("tcp:[ $tcpAddress ]")
        initZContext()

        if (mReceiverFlag.get()) {
            trace("client already linked success ,break!")
            return
        }

        mJob = mScope.launch {
            runCatching {
                mSocketReceiver = mContext?.createSocket(SocketType.PAIR)
                trace("create socket success!")
                var connected = false
                mSocketReceiver?.let { socket ->
                    socket.sendTimeOut = 3000
                    runCatching {
                        connected = socket.bind(tcpAddress)
                        trace("bind success!")
                    }.onFailure {
                        connected = false
                        it.printStackTrace()
                        trace("bind failure:$it")
                    }

                    if (!connected) {
                        trace("bind address failure,break --->")
                    }
                    mScope.launch {
                        while (!Thread.currentThread().isInterrupted && !mLoopFlag.get()) {
                            runCatching {
                                val receiver = socket.recv(0)
                                if (!mReceiverFlag.get()) {
                                    trace("【 client bind success ！】")
                                }
                                mReceiverFlag.set(true)
                                if (receiver != null) {
                                    val content = String(receiver, ZMQ.CHARSET)
                                    mReceiverListener?.onCallBack(content)
                                }
                            }.onFailure {
                                it.printStackTrace()
                                trace("receiver failure :$it")
                                mReceiverFlag.set(false)
                            }
                        }
                    }
                }

            }.onFailure {
                it.printStackTrace()
                trace("init socket failure :$it")
            }
        }
    }

    fun send(): Boolean {
        runCatching {
            val response = "接收端-->发送-->：(${mNumber})"
            if (mReceiverFlag.get()) {
                mSocketReceiver?.send(response.toByteArray(ZMQ.CHARSET), 0)
                mNumber++
                mSendListener?.onCallBack(response)
                return true
            } else {
                trace("bind address is failure ,cant send!")
            }
        }.onFailure {
            it.printStackTrace()
            trace("send message failure: : $it")
        }
        return false
    }

    fun stop() {
        mReceiverFlag.set(false)
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
        if (!TextUtils.equals(mTraceOldMsg, content)) {
            mTraceInfo += (content + "\n\n")
            mTraceListener?.onCallBack(mTraceInfo)
            mTraceOldMsg = content
        }
        LogUtil.e(ZmqUtil.TAG, content)
    }
}