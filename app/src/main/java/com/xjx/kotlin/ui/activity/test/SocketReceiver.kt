package com.xjx.kotlin.ui.activity.test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.android.common.utils.LogUtil

class SocketReceiver : BroadcastReceiver() {

    private lateinit var mContext: Context;

    override fun onReceive(context: Context, intent: Intent) {
        mContext = context

        val extras = intent.extras
        extras?.let {
            val ip = it.get("ip")
            LogUtil.e("ip:$ip  ")

            Toast.makeText(mContext, "接收到的Ip为:$ip", Toast.LENGTH_LONG).show();
        }
    }
}