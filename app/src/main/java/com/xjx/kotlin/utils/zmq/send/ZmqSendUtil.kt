package com.xjx.kotlin.utils.zmq.send

import com.android.common.utils.LogUtil
import com.xjx.kotlin.utils.zmq.TCP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ

class ZmqSendUtil {

    private val context: ZMQ.Context = ZMQ.context(1)
    private val socket: ZMQ.Socket = context.socket(SocketType.REP)
    private val mContext: ZContext? = null

    private val mScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(Dispatchers.IO)
    }

    fun connect(block: (Boolean) -> Unit) {
        val connect = socket.connect(TCP.TCP_ADDRESS)
        block(connect)
    }

    fun send(content: String) {
        mScope.launch {
            LogUtil.e("ZMQ", "发送数据--->$content")
            val toByteArray = content.toByteArray(ZMQ.CHARSET)
            socket.send(toByteArray, 1)
        }
    }

    fun close() {
        LogUtil.e("ZMQ", "发送端 close --->")
        socket.close()
        context.close()
    }
}