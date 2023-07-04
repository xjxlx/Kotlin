package com.xjx.kotlin.utils.zmq.send

import com.xjx.kotlin.utils.zmq.receive.ZmqUtil2
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
        val connect = socket.connect("tcp://${ip}:19716")
        block(connect)
    }

    fun send(content: String) {
        mScope.launch {
            ZmqUtil2.log("发送数据--->$content")
            val toByteArray = content.toByteArray(ZMQ.CHARSET)
            socket.send(toByteArray, 1)
        }
    }

    fun close() {
        ZmqUtil2.log("发送端 close --->")
        socket.close()
        context.close()
    }
}