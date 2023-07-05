package com.xjx.kotlin.utils.zmq

import android.text.TextUtils

object TCP {

    var ip_address = ""
    val TCP_ADDRESS: String
        get() {
            if (TextUtils.isEmpty(ip_address)) {
                return ""
            }
            return "tcp://${ip_address}:5555"
        }
}