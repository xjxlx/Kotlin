package com.xjx.kotlin.ui.activity.test

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.android.app.KeepAidlInterface
import com.android.app.OnChangeListenerAidlInterface
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityAidlBinding

class AidlActivity : BaseBindingTitleActivity<ActivityAidlBinding>() {

    private lateinit var mKeepIntent: Intent

    private var listener: OnChangeListenerAidlInterface = object : OnChangeListenerAidlInterface.Stub() {
        override fun onChange(flag: Int) {
            LogUtil.e("flag ----> $flag")
        }
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            LogUtil.e("ServiceConnection ---> ")
            // 固定写法
            val keepAidlInterface = KeepAidlInterface.Stub.asInterface(service)
            keepAidlInterface?.let { it.setOnChangeListener(listener) }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            LogUtil.e("onServiceDisconnected ---> ")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initData(savedInstanceState: Bundle?) {
        LogUtil.e("initData")
        // aidl
        mKeepIntent = Intent()
        mKeepIntent.action = "com.xjx.keep"
        mKeepIntent.setPackage("com.android.app.free.debug")
        val bindService = bindService(mKeepIntent, mConnection, Context.BIND_AUTO_CREATE)
        LogUtil.e("bindService::: $bindService")

        mBinding.tvAidl.text = "结果：$bindService"
    }

    override fun getTitleContent(): String {
        return "AIDL 调用"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityAidlBinding {
        return ActivityAidlBinding.inflate(inflater, container, true)
    }

    override fun onDestroy() {
        super.onDestroy()

        unbindService(mConnection)
    }
}
