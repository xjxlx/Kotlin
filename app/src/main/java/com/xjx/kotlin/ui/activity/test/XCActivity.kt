package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityXcactivityBinding
import kotlinx.coroutines.*

class XCActivity : AppBaseBindingTitleActivity<ActivityXcactivityBinding>() {
    private var TAG = "XC"

    override fun setTitleContent(): String {
        return "协程"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityXcactivityBinding {
        return ActivityXcactivityBinding.inflate(inflater, container, true)
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

        GlobalScope.launch(Dispatchers.Main) {
            LogUtil.e("~~~~~~~~~~》》》》》：我是线程：！！！")
            withContext(Dispatchers.IO) {
                delay(1000)
                LogUtil.e("~~~~~~~~~~》》》》》：异步线程：！！！")
            }
            LogUtil.e("~~~~~~~~~~》》》》》：我是线程 ---> 结果：！！！")
        }

        //  GlobalScope.launch(Dispatchers.Main) {
        //    Log.e("Tag", "xxxxxx1 in " + Thread.currentThread().name)
        //    doIO()
        //    Log.e("Tag", "xxxxxx3 in " + Thread.currentThread().name)
        // }

        // cancel kotlin  coroutines
        GlobalScope.launch(Dispatchers.Main) {
            val job = GlobalScope.launch(Dispatchers.Default) {
                repeat(10) {
                    LogUtil.e("cancel", "xxxix - $it")
                    delay(500)
                }
            }
            delay(2000)
            // job.cancel()
            LogUtil.e("cancel", "xxxix - job wait")
            job.join()
            LogUtil.e("cancel", "xxxix - job finish")
        }

        // time out or null
        val jobTimeOutNull = GlobalScope.launch(Dispatchers.Main) {
            TAG = "null or time out "
            LogUtil.e(TAG, "time out null launch ...")
            val timeOut = timeOutOrNull()
            LogUtil.e(TAG, "time out null result ---> $timeOut")
        }
        jobTimeOutNull.cancel()

        // time out
        GlobalScope.launch {
            TAG = "time out "
            LogUtil.e(TAG, "time out launch --->")

            val timeout = timeout()
            LogUtil.e(TAG, "time out ---> $timeout")
        }

        // test synchronization
        val launchSynchronization = GlobalScope.launch {
            TAG = "test synchronization"
            LogUtil.e(TAG, "synchronization launch ...")
            val testSynchronization1 = testSynchronization1()
            val testSynchronization2 = testSynchronization2()
            LogUtil.e(TAG, "synchronization launch --- result ---> $testSynchronization1 --- $testSynchronization2")
        }
        launchSynchronization.cancel()

        // test asynchronous
        GlobalScope.launch {
            TAG = "test-asynchronous"
            LogUtil.e(TAG, "asynchronous launch ...")
            val startTime = System.currentTimeMillis()
            val async1 = async {
                testAsynchronous1()
            }
            val async2 = async {
                testAsynchronous2()
            }
            val intervalTime = System.currentTimeMillis() - startTime
            LogUtil.e(TAG, "asynchronous launch --- result ---> ${async1.await()} --- ${async2.await()}" + "   intervalTime:  " + intervalTime)
        }
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
}