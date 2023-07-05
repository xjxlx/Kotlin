package com.xjx.kotlin.utils.zmq

import android.text.TextUtils

object TCP {
    var ip_address = "192.168.124.5"
    private const val ZmqPort: Int = 9999
    val SocketPort: Int = 9998

    val TCP_ADDRESS: String
        get() {
            if (TextUtils.isEmpty(ip_address)) {
                return ""
            }
            return "tcp://${ip_address}:$ZmqPort"
        }
}