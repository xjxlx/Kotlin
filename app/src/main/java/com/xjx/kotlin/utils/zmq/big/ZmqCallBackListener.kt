package com.xjx.kotlin.utils.zmq.big

interface ZmqCallBackListener {
    fun onCallBack(content: String)
}