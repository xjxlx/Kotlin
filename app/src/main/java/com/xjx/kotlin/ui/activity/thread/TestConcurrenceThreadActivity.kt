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

        @Volatile
    private var isPauseFlag: Boolean = false

    private val mScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(Dispatchers.IO)
    }
    private val mJob1: Job by lazy {
        return@lazy mScope.launch(start = CoroutineStart.LAZY) {
            sendData("JOB - 1")
        }
    }
    private val mJob2: Job by lazy {
        return@lazy mScope.launch(start = CoroutineStart.LAZY) {
            sendData("JOB - 2")
        }
    }
    private val mJob3: Job by lazy {
        return@lazy mScope.launch(start = CoroutineStart.LAZY) {
            sendData("JOB - 3")
        }
    }
    private val mJob4: Job by lazy {
        return@lazy mScope.launch(start = CoroutineStart.LAZY) {
            sendData("JOB - 4")
        }
    }
    private var mSendChannel: SendChannel<String>? = null

    private fun sendData(tag: String) {
        LogUtil.e("tag: $tag - thread: ${Thread.currentThread().name}")
        mScope.launch {
            repeat(Int.MAX_VALUE) {
                mSendChannel?.send("我是tag: $tag  ---> 我是item : $it")
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
                    if (iterator.hasNext() && !isPauseFlag) {
                        val next = iterator.next()
                        LogUtil.e(" 收集 ---> $next")
                    }
                }
            }
        }
    }

    @Synchronized
    private fun start() {
        isPauseFlag = false
        mJob1.start()
        mJob2.start()
        mJob3.start()
        mJob4.start()
    }

    @Synchronized
    private fun pause() {
        isPauseFlag = true
        mJob1.cancel()
        mJob2.cancel()
        mJob3.cancel()
        mJob4.cancel()
        LogUtil.e(" 暂停了   当前的flag ---> pause : true")
    }
}