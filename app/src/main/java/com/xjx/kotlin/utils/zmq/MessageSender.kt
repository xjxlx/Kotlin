package com.xjx.kotlin.utils.zmq

import android.os.Handler
import com.xjx.kotlin.utils.zmq.send.ZMQMessageSender

class MessageSender(private val handler: Handler) {

    fun sendMessage(message: String) {
        Thread(ZMQMessageSender(handler, message)).start()
    }
}