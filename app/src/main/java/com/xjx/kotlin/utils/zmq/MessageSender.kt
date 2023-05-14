package com.xjx.kotlin.utils.zmq.send

import android.os.Handler

class MessageSender(private val handler: Handler) {

    fun sendMessage(message: String) {
        Thread(ZMQMessageSender(handler, message)).start()
    }
}