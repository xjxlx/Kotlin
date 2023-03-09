package com.xjx.kotlin.ui.activity.test.xc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityXcactivityBinding
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class XCActivity : AppBaseBindingTitleActivity<ActivityXcactivityBinding>() {
    private var TAG = "XC"
    private lateinit var launchTestAsynchronous: Job

    override fun setTitleContent(): String {
        return "协程"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityXcactivityBinding {
        return ActivityXcactivityBinding.inflate(inflater, container, true)
    }

    override fun initListener() {
        super.initListener()

        mBinding.btnStart.setOnClickListener {
            launchTestAsynchronous = GlobalScope.launch {
                TAG = "test-asynchronous"
                LogUtil.e(TAG, "asynchronous launch ...")
                val measureTimeMillis = measureTimeMillis {
                    val async1 = async {
                        testAsynchronous1()
                    }
                    val async2 = async {
                        testAsynchronous2()
                    }
                    LogUtil.e(TAG, "asynchronous launch --- result ---> ${async1.await()} --- ${async2.await()}")
                }
                LogUtil.e(TAG, "asynchronous launch --- time --->  $measureTimeMillis")
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {

//         1:构建全局的协程
//        GlobalScope.launch() {
//            LogUtil.e(TAG, "1: 我是默认线程的 --- 全局的协程，默认线程： Thread: " + Thread.currentThread().name)
//            // 1.1 :非阻塞的挂起函数，不会阻塞线程，但是会挂起协程，并且智能在协程中使用
//            delay(1000)
//            LogUtil.e(TAG, "1: 我是默认线程的 --- 全局的协程！ --- hello")
//        }

//        // 2：全局协程 --- 指定线程
//        GlobalScope.launch(Dispatchers.Main) {
//            LogUtil.e(TAG, "2: 我是指定线程的全局协程！ Thread :" + Thread.currentThread().name)
//        }

//        // 3：全局协程 --- 指定线程
//        GlobalScope.launch(Dispatchers.Unconfined) {
//            LogUtil.e(TAG, "3: 我是指定线程的全局协程！ Thread :" + Thread.currentThread().name)
//        }

//        // 4: 构建一个阻塞的协程
//        runBlocking {
//            LogUtil.e(TAG, "4: 构建阻塞的协程 ！ Thread :" + Thread.currentThread().name)
//            delay(3000)
//            LogUtil.e(TAG, "4.1 : 阻塞协程的等待")
//        }

//        GlobalScope.launch(Dispatchers.Main) {
//            LogUtil.e("~~~~~~~~~~》》》》》：我是线程：！！！")
//            withContext(Dispatchers.IO) {
//                delay(1000)
//                LogUtil.e("~~~~~~~~~~》》》》》：异步线程：！！！")
//            }
//            LogUtil.e("~~~~~~~~~~》》》》》：我是线程 ---> 结果：！！！")
//        }

        // cancel kotlin coroutines
//        GlobalScope.launch(Dispatchers.Main) {
//            val job = GlobalScope.launch(Dispatchers.Default) {
//                repeat(10) {
//                    LogUtil.e("cancel", "xxxix - $it")
//                    delay(500)
//                }
//            }
//            delay(2000)
//            // job.cancel()
//            LogUtil.e("cancel", "xxxix - job wait")
//            job.join()
//            LogUtil.e("cancel", "xxxix - job finish")
//        }

        // time out or null
//        val jobTimeOutNull = GlobalScope.launch(Dispatchers.Main) {
//            TAG = "null or time out "
//            LogUtil.e(TAG, "time out null launch ...")
//            val timeOut = timeOutOrNull()
//            LogUtil.e(TAG, "time out null result ---> $timeOut")
//        }
//        jobTimeOutNull.cancel()

        // time out
//        GlobalScope.launch {
//            TAG = "time out "
//            LogUtil.e(TAG, "time out launch --->")
//
//            val timeout = timeout()
//            LogUtil.e(TAG, "time out ---> $timeout")
//        }

        // test synchronization
//        val launchSynchronization = GlobalScope.launch {
//            TAG = "test synchronization"
//            LogUtil.e(TAG, "synchronization launch ...")
//            val measureTimeMillis = measureTimeMillis {
//                val testSynchronization1 = testSynchronization1()
//                val testSynchronization2 = testSynchronization2()
//                LogUtil.e(TAG, "synchronization launch --- result ---> $testSynchronization1 --- $testSynchronization2")
//            }
//            LogUtil.e(TAG, "synchronization launch --- measureTimeMillis ---> $measureTimeMillis")
//        }
//        launchSynchronization.cancel()

        /**
         * activity / fragment 的扩展
         */
        lifecycleScope.launch {

        }

        // test run blocking
//        TAG = " run - block - ing"
//        runBlocking {
//            LogUtil.e(TAG, "run block ing launch ... ")
//            val millis = measureTimeMillis {
//                delay(1000)
//            }
//            LogUtil.e(TAG, "run block ing result ...> time: $millis")
//        }

        // test join
//        GlobalScope.launch {
//            TAG = " test - join "
//
//            LogUtil.e(TAG, "run join launch ... ")
//
//            val job1 = GlobalScope.launch {
//                repeat(70) {
//                    LogUtil.e(TAG, "run join repeat - 1 ... $it")
//                }
//            }
//
//            val job2 = GlobalScope.launch {
//                repeat(30) {
//                    LogUtil.e(TAG, "run join repeat - 2 ... $it")
//                }
//            }
//
//            LogUtil.e(TAG, "run join start join ... ")
//            job1.join()
//            job2.join()
//            LogUtil.e(TAG, "run join finish  ... ")
//        }

        // test_zyy
//        TAG = " test_zyy "
//        test_zyy_1()

//        TAG = " test_zyy -2 "
//        test_zyy_2()

//        TAG = " test_zyy - 3 "
//        test_zyy_3()

        TAG = " test_zyy - 4 "
        test_zyy_4()
    }

    private suspend fun doIO() {
        withContext(Dispatchers.IO) {
            Thread.sleep(1000)
            Log.e("Tag", "xxxxxx2 in " + Thread.currentThread().name)
        }
    }

    private suspend fun timeOutOrNull(): String? {
        val withTimeoutOrNull = withTimeoutOrNull(4000) {
            delay(3999)
            "time out null"
        }
        return withTimeoutOrNull
    }

    private suspend fun timeout(): String {
        val timeOut = 4001L
        val withTimeout = withTimeout(4000) {
            delay(timeOut)
            "time out "
        }
        return withTimeout
    }

    private suspend fun testSynchronization1(): String {
        delay(2000)
        return "test - 1"
    }

    private suspend fun testSynchronization2(): String {
        delay(2000)
        return "test - 2"
    }

    private suspend fun testAsynchronous1(): String {
        delay(2000)
        return "test - 1"
    }

    private suspend fun testAsynchronous2(): String {
        delay(2000)
        return "test - 2"
    }

    private fun test_zyy_1() = runBlocking {
        LogUtil.e(TAG, "test_zyy ... luanch !")
        launch { // 在 runBlocking 作用域中启动一个新协程
            delay(1000L)
            LogUtil.e(TAG, "World!")
        }
        LogUtil.e(TAG, "Hello,")
    }

    private fun test_zyy_2() = GlobalScope.launch {
        LogUtil.e(TAG, "test_zyy -2  ... luanch !")
        repeat(100) {
            LogUtil.e(TAG, "test_zyy -2  ... repeat:  $it")
            delay(500)
        }

        launch { // 在 runBlocking 作用域中启动一个新协程
            delay(1000L)
            LogUtil.e(TAG, "World!")
        }
        LogUtil.e(TAG, "Hello,")
    }

    private fun test_zyy_3() = runBlocking { // this: CoroutineScope
        coroutineScope { // 创建一个协程作用域
            launch {
                delay(500L)
                LogUtil.e(TAG, "Task from nested launch")
            }

            delay(100L)
            LogUtil.e(TAG, "Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }
        LogUtil.e(TAG, "Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
    }

    private fun test_zyy_4() = runBlocking { // this: CoroutineScope
        GlobalScope.launch {
            repeat(100) { i ->
                LogUtil.e(TAG, "I'm sleeping $i ..." + this.isActive)
                delay(500L)
            }
        }
        delay(1300L) // 在延迟后退出
        LogUtil.e(TAG, "Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::launchTestAsynchronous.isInitialized) {
            val active = launchTestAsynchronous.isActive
            LogUtil.e(TAG, "asynchronous launch ---  active ---> $active")
            launchTestAsynchronous.cancel()
        }
    }
}