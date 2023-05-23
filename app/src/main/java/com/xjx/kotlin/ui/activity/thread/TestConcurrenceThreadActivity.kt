package com.xjx.kotlin.ui.activity.thread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.apphelper2.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityTestConcurrenceThreadBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor

class TestConcurrenceThreadActivity : AppBaseBindingTitleActivity<ActivityTestConcurrenceThreadBinding>() {

    private var isPauseFlag = false
    private val mScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(Dispatchers.IO)
    }
    private val mJob: Job by lazy {
        return@lazy mScope.launch(start = CoroutineStart.LAZY) {
            sendData()
        }
    }
    private var mSendChannel: SendChannel<String>? = null

    private fun sendData() {
        mScope.launch {
            repeat(Int.MAX_VALUE) {
                mSendChannel?.send("我是item : $it")
                delay(50)
            }
        }
    }

    override fun setTitleContent(): String {
        return "测试并发的线程"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityTestConcurrenceThreadBinding {
        return ActivityTestConcurrenceThreadBinding.inflate(inflater, container, true)
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnStart.setOnClickListener {
            start()
        }
        mBinding.btnPause.setOnClickListener {
            pause()
        }
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    override fun initData(savedInstanceState: Bundle?) {
        mScope.launch {
            mSendChannel = actor {
                val iterator = iterator()
                while (true) {
                    if (!isPauseFlag) {
                        if (iterator.hasNext()) {
                            val next = iterator.next()
                            LogUtil.e(" 收集 ---> $next")
                        }
                    }
                }
            }
        }
    }

    private fun start() {
        if (!mJob.isActive) {
            isPauseFlag = false
            mJob.start()
        }
    }

    private fun pause() {
        isPauseFlag = true
        LogUtil.e(" 暂停了")
    }
}