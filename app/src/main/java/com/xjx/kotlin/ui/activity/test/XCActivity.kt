package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityXcactivityBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class XCActivity : AppBaseBindingTitleActivity<ActivityXcactivityBinding>() {
    private val TAG = "XC"

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
//
//        // 2：全局协程 --- 指定线程
//        GlobalScope.launch(Dispatchers.Main) {
//            LogUtil.e(TAG, "2: 我是指定线程的全局协程！ Thread :" + Thread.currentThread().name)
//        }
//
//        // 3：全局协程 --- 指定线程
//        GlobalScope.launch(Dispatchers.Unconfined) {
//            LogUtil.e(TAG, "3: 我是指定线程的全局协程！ Thread :" + Thread.currentThread().name)
//        }
//
//        // 4: 构建一个阻塞的协程
//        runBlocking {
//            LogUtil.e(TAG, "4: 构建阻塞的协程 ！ Thread :" + Thread.currentThread().name)
//            delay(3000)
//            LogUtil.e(TAG, "4.1 : 阻塞协程的等待")
//        }

//          main()

        GlobalScope.launch {
            job()
        }
    }

    private fun main() = runBlocking<Unit> { // 1：开始执行一个阻塞的协程
        LogUtil.e(TAG, "开始执行阻塞协程")
        GlobalScope.launch { // 2: 启动一个新的协程
            LogUtil.e(TAG, "启动了一个新的协程")
        }
        LogUtil.e(TAG, "主线程执行")
    }

    private suspend fun job() {
        val job = GlobalScope.launch {
            LogUtil.e(TAG, "hello ")
        }
        job.join()
        LogUtil.e(TAG, "word ")
    }

}