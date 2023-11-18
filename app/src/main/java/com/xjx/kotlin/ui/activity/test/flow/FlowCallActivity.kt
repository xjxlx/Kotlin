package com.xjx.kotlin.ui.activity.test.flow

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.android.common.utils.LogWriteUtil
import com.android.common.utils.permission.PermissionUtil
import com.xjx.kotlin.databinding.ActivityFlowCallBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class FlowCallActivity : BaseBindingTitleActivity<ActivityFlowCallBinding>() {

    private val mSharedFlow = MutableSharedFlow<String>()
    private val mPermission = PermissionUtil.PermissionActivity(this)
    private val scope = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null
    private var count = 0
    private var isSendFlag = false
    private val mWrite: LogWriteUtil by lazy {
        return@lazy LogWriteUtil("test.txt").apply { init(this@FlowCallActivity) }
    }
    private val mStateFlow2: MutableSharedFlow<Bean> = MutableStateFlow(Bean("张三", 12))

    override fun getTitleContent(): String {
        return "Flow Call"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityFlowCallBinding {
        return ActivityFlowCallBinding.inflate(inflater, container, true)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initListener() {
        super.initListener()
        mBinding.btnStart.setOnClickListener {
            isSendFlag = true
            //            KeepLifeBroadCast.closeListener()
            LogUtil.e("start ---> collect ----> ")

            //            job?.let {
            //                if (it.isActive) {
            //                    it.cancel()
            //                }
            //            }

            //            job = scope.launch {
            //                repeat(Int.MAX_VALUE) {
            //                    if (isSendFlag) {
            //                        mSharedFlow.emit("当前item: $it")
            ////                        delay(100)
            //                    }
            //                }
            //            }

            lifecycleScope.launch {
                mStateFlow2.collectLatest {
                    LogUtil.e("lastOrNull ->$it")
                    it.age++
                    mStateFlow2.emit(it)
                }
            }
        }

        mBinding.btnPause.setOnClickListener {
            isSendFlag = false
            mWrite.send("close -----pause ---->")
            //            val appInstallApp = SystemUtil.appInstallApp(this, "com.android.poc")

            lifecycleScope.launch(Dispatchers.IO) {
                //                KeepLifeBroadCast.sendAppErrorBroadcast(this@FlowCallActivity,
                // "com.android.poc")
                //                KeepLifeBroadCast.sendAppPollListenerBroadcast(this@FlowCallActivity,
                // "com.android.poc", 9500)
                // SystemUtil.openApplication(this@FlowCallActivity, "com.android.poc")

                mStateFlow2.collectLatest { LogUtil.e("last or null", it) }
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
                    mSharedFlow.collect() { LogUtil.e("collect ----> $it") }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mWrite.send("onDestroy")
    }

    data class Bean(var name: String, var age: Int) {}
}
