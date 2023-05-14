package com.xjx.kotlin.utils.zmq.send

import android.os.Handler
import org.zeromq.SocketType
import org.zeromq.ZMQ

class ZMQMessageSender(private val handler: Handler, private val message: String) : Runnable {

    override fun run() {
        val context: ZMQ.Context = ZMQ.context(1)
        val socket: ZMQ.Socket = context.socket(SocketType.REQ)
        socket.connect(ZmqUtil.TCP_ADDRESS)
        socket.send(message.toByteArray(ZMQ.CHARSET), ZmqUtil.ZMQ_WAIT)

        handler.sendMessage(ZmqUtil.bundledMessage(handler, message))

        socket.close()
        context.close()
    }
}