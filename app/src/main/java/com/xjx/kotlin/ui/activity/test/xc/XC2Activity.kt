package com.xjx.kotlin.ui.activity.test.xc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityXc2Binding
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class XC2Activity : BaseBindingTitleActivity<ActivityXc2Binding>() {
    private var TAG = "XC - 2 "
    private val mainScope = MainScope()

    override fun getTitleContent(): String = "协程 - 2"

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): ActivityXc2Binding = ActivityXc2Binding.inflate(inflater, container, true)

    override fun initListener() {
        super.initListener()
        mBinding.btnStart.setOnClickListener {
            GlobalScope.launch {
                delay(1000)
                LogUtil.e(TAG, "test.....")
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        // test_1()

        // test_2()

        // test_3()

        // 惰性启动的 async
        //  test_async()

        // 结构化并发异常的测试
        // asyncMoreBf()

        // test Dispatchers
        // test_Dispatchers()

        // test_coroutineName()
        // test_coroutineName()

        // test coroutine scope
        //        GlobalScope.launch {
        //            test_CoroutineScope()
        //        }
    }

    private suspend fun test_CoroutineScope() {
        coroutineScope {
            LogUtil.e(TAG, "test_CoroutineScope ---> coroutineScope ---> thread :" + Thread.currentThread().name)
        }

        mainScope.launch {
            LogUtil.e(TAG, "test_CoroutineScope ---> mainScope ---> thread :" + Thread.currentThread().name)
        }
    }

    private fun test_coroutineName() {
        lifecycleScope.launch {
            val async1 =
                async(CoroutineName("async - 1")) {
                    delay(2000)
                    LogUtil.e(TAG, "async -----> result -1")
                    "result -1"
                }
            val async2 =
                async(CoroutineName("async - 2")) {
                    delay(2000)
                    LogUtil.e(TAG, "async -----> result -2")
                    "result -2"
                }
            LogUtil.e(TAG, "result ---> " + async1.await() + " --- " + async2.await())
            LogUtil.e(TAG, "result ---> ")
        }
    }

    private fun test_1() =
        runBlocking {
            val job =
                launch(Dispatchers.Default) {
                    repeat(100) {
                        LogUtil.e(TAG, "job: I'm sleeping $it ...")
                        delay(500)
                    }
                }
            delay(1300L) // 等待一段时间
            LogUtil.e(TAG, "main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消一个作业并且等待它结束
            LogUtil.e(TAG, "main: Now I can quit.")
        }

    private fun test_2() =
        runBlocking {
            val job =
                launch(Dispatchers.Default) {
                    repeat(100) {
                        if (isActive) {
                            LogUtil.e(TAG, "job: I'm sleeping $it ...")
                        }
                        delay(500)
                    }
                }
            delay(1300L) // 等待一段时间
            LogUtil.e(TAG, "main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消一个作业并且等待它结束
            LogUtil.e(TAG, "main: Now I can quit.")
        }

    private fun test_3() =
        runBlocking {
            val job =
                launch {
                    try {
                        repeat(1000) { i ->
                            LogUtil.e(TAG, "job: I'm sleeping $i ...")
                            delay(500L)
                        }
                    } finally {
                        withContext(NonCancellable) {
                            LogUtil.e(TAG, "job: I'm running finally")
                            delay(1000L)
                            LogUtil.e(TAG, "job: And I've just delayed for 1 sec because I'm non-cancellable")
                        }
                    }
                }
            delay(1300L) // 延迟一段时间
            LogUtil.e(TAG, "main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消该作业并等待它结束
            LogUtil.e(TAG, "main: Now I can quit.")
        }

    private fun test_async() {
        lifecycleScope.launch {
            LogUtil.e(TAG, "test_async  launch ...")
            val time =
                measureTimeMillis {
                    val async1 = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
                    val async2 = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
                    // 如果async 设置了惰性启动，则会在 .await() 的时候，或者 .start()的时候，才会去启动
                    //                async1.start()
                    //                async2.start()
                    LogUtil.e(TAG, "test_async ---> " + async1.await() + " --- " + async2.await())
                }
            LogUtil.e(TAG, "test_async --->  time : $time")
        }
    }

    private suspend fun doSomethingUsefulOne(): String {
        delay(1000)
        return "doSomethingUsefulOne"
    }

    private suspend fun doSomethingUsefulTwo(): String {
        delay(2000)
        return "doSomethingUsefulTwo"
    }

    private suspend fun failedConcurrentSum(): Int =
        coroutineScope {
            val one =
                async {
                    try {
                        delay(Long.MAX_VALUE) // 模拟一个长时间的运算
                        42
                    } finally {
                        LogUtil.e(TAG, "First child was cancelled")
                    }
                }

            val two =
                async<Int> {
                    LogUtil.e(TAG, "Second child throws an exception")
                    throw ArithmeticException()
                }
            one.await() + two.await()
        }

    private fun asyncMoreBf() {
        lifecycleScope.launch {
            try {
                failedConcurrentSum()
            } catch (e: ArithmeticException) {
                LogUtil.e(TAG, "Computation failed with ArithmeticException")
            }
        }
    }

    private fun test_Dispatchers() {
        lifecycleScope.launch(Dispatchers.IO) {}

        lifecycleScope.launch(newSingleThreadContext("")) {}
    }
}
