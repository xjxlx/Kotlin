package com.xjx.kotlin.utils.zmq.send

interface ZmqMessageListener {
    fun onMessageReceived(messageBody: String)
}