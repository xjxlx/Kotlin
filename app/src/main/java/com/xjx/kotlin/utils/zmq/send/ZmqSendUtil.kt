package com.xjx.kotlin.utils.zmq.send

import com.android.apphelper2.utils.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.zeromq.SocketType
import org.zeromq.ZMQ

class ZmqSendUtil {

    private val context: ZMQ.Context = ZMQ.context(1)
    private val socket: ZMQ.Socket = context.socket(SocketType.PAIR)

    private val mScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(Dispatchers.IO)
    }

    fun connect(ip: String, block: (Boolean) -> Unit) {
        val connect = socket.connect("tcp://${ip}:10086")
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