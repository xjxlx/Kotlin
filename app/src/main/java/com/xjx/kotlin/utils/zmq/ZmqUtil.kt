package com.xjx.kotlin.utils.zmq

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import com.android.common.utils.LogUtil
import com.xjx.kotlin.utils.zmq.send.ZmqMessageListener

/**
 * Zmq util
 */
class ZmqUtil {

    private val mSenderHandler: ZmqHandler by lazy {
        return@lazy ZmqHandler(Looper.getMainLooper(), object : ZmqMessageListener {
            override fun onMessageReceived(messageBody: String) {
                LogUtil.e(TAG, "result --->  send: message :$messageBody")
            }
        }, KEY_PAYLOAD)
    }

    private val mSender: MessageSender by lazy {
        return@lazy MessageSender(mSenderHandler)
    }

    companion object {
        private const val FileName = "ZMQ"
        private const val KEY_PAYLOAD: String = "payloadKey"
        const val TAG = "ZMQ"
        const val ZMQ_WAIT: Int = 0
        var IP_ADDRESS = ""

        // true:open  false:close
        const val ZMQ_SWITCH = false


        fun bundledMessage(handler: Handler, msg: String): Message {
            val message: Message = handler.obtainMessage()
            val bundle = Bundle()
            bundle.putString(KEY_PAYLOAD, msg)
            message.data = bundle
            return message
        }

        fun log(content: String) {
            LogUtil.e(TAG, content)
        }
    }

    private val mReceiverHandler by lazy {
        log("mReceiverHandler create！")
        ZmqHandler(Looper.getMainLooper(), object : ZmqMessageListener {
            override fun onMessageReceived(messageBody: String) {
                LogUtil.e(TAG, "result --->  receiver: message : $messageBody")
                try {

                } catch (e: Exception) {
                    log("json error! msg:${e.message}")
                }
            }
        }, KEY_PAYLOAD)
    }

    fun sendMessage(content: String) {
        if (!TextUtils.isEmpty(content)) {
            mSender.sendMessage(content)
        }
    }
}
