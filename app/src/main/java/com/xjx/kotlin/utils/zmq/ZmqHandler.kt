package com.xjx.kotlin.utils.zmq

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.xjx.kotlin.utils.zmq.send.ZmqMessageListener

class ZmqHandler(looper: Looper, private val messageListener: ZmqMessageListener, private val message: String) : Handler(looper) {

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)

        msg.data.getString(message)
            ?.let { messageListener.onMessageReceived(it) }
    }
}