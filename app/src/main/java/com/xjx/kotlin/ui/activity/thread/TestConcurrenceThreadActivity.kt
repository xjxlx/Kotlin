package com.xjx.kotlin.ui.activity.thread

import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import com.android.apphelper2.utils.LogUtil
import com.xjx.kotlin.R
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor

class TestConcurrenceThreadActivity : FragmentActivity() {

//    private val atomicBoolean: AtomicBoolean = AtomicBoolean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_concurrence_thread)

        initData()
    }

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

    fun initData() {
        findViewById<Button>(R.id.btn_start).setOnClickListener {
            start()
        }
        findViewById<Button>(R.id.btn_pause).setOnClickListener {
            pause()
        }

        mScope.launch {
            mSendChannel = actor {
                val iterator = iterator()
                while (true) {
                        if (iterator.hasNext()) {
                            val next = iterator.next()
                            if (!isPauseFlag) {
                                LogUtil.e(" 收集 ---> $next")
                            }
                        }
                }
            }
        }
    }

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