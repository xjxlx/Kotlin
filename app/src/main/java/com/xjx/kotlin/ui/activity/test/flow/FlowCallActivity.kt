package com.xjx.kotlin.ui.activity.test.flow

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.KeepLifeUtil
import com.android.apphelper2.utils.LogUtil
import com.android.apphelper2.utils.LogWriteUtil
import com.android.apphelper2.utils.permission.PermissionUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityFlowCallBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow

class FlowCallActivity : AppBaseBindingTitleActivity<ActivityFlowCallBinding>() {

    private val mSharedFlow = MutableSharedFlow<String>()
    private val mPermission = PermissionUtil.PermissionActivity(this)
    private val scope = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null
    private var count = 0
    private var isSendFlag = false
    private val mWrite: LogWriteUtil by lazy {
        return@lazy LogWriteUtil(this, "test.txt")
    }

    override fun setTitleContent(): String {
        return "Flow Call"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityFlowCallBinding {
        return ActivityFlowCallBinding.inflate(inflater, container, true)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initListener() {
        super.initListener()
        mBinding.btnStart.setOnClickListener {
            isSendFlag = true
            LogUtil.e("start ---> collect ----> ")

            job?.let {
                if (it.isActive) {
                    it.cancel()
                }
            }

            job = scope.launch {
                repeat(Int.MAX_VALUE) {
                    if (isSendFlag) {
                        mSharedFlow.emit("当前item: $it")
//                        delay(100)
                    }
                }
            }
        }

        mBinding.btnPause.setOnClickListener {
            isSendFlag = false
            LogUtil.e("pause ---> collect ----> ")

            mWrite.write("close -----pause ---->")
//            val appInstallApp = SystemUtil.appInstallApp(this, "com.android.poc")

            lifecycleScope.launch {
                LogUtil.e("开始执行 --->")
                KeepLifeUtil.sendAppRunningBroadcast(this@FlowCallActivity, "com.android.poc")
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
//        job = scope.launch {
//            while (true) {
//                LogUtil.e("interval ----> $count")
//                count++
//                delay(1000)
//            }
//        }
//
//        scope.launch {
//            LogUtil.e("thread ---> 1 --> ${ Thread.currentThread().name }")
//        }
//        scope.launch {
//            LogUtil.e("thread ---> 2 --> ${ Thread.currentThread().name }")
//        }
//        scope.launch {
//            LogUtil.e("thread ---> 3 --> ${ Thread.currentThread().name }")
//        }

        scope.launch {
            while (true) {
                if (isSendFlag) {
                    mSharedFlow.collect() {
                        LogUtil.e("collect ----> $it")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mWrite.write("onDestroy")
    }
}