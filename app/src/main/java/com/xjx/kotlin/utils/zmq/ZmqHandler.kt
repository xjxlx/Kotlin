package com.xjx.kotlin.utils.zmq.send

import android.os.Handler
import android.os.Looper
import android.os.Message

class ZmqHandler(looper: Looper, private val messageListener: ZmqMessageListener, private val message: String) : Handler(looper) {

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)

        msg.data.getString(message)
            ?.let { messageListener.onMessageReceived(it) }
    }
}