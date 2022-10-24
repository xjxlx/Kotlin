package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityXcactivityBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class XCActivity : AppBaseBindingTitleActivity<ActivityXcactivityBinding>() {

    override fun setTitleContent(): String {
        return "协程"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityXcactivityBinding {
        return ActivityXcactivityBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
            LogUtil.e("World!1") // 在延迟后打印输出
        }

        LogUtil.e("Hello,1") // 协程已在等待时主线程还在继续
        Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活

        /**
         * ---------------------------------------------------
         */

        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            LogUtil.e("World2!")
        }
        LogUtil.e("Hello2,") // 主线程中的代码会立即执行
        runBlocking {     // 但是这个表达式阻塞了主线程
            delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
            LogUtil.e("--=----=.> 2,") // 主线程中的代码会立即执行
        }

        main()
    }

    suspend fun test() {
        val job = GlobalScope.launch { // 启动一个新协程并保持对这个作业的引用
            delay(1000L)
            LogUtil.e("World!")
        }
        LogUtil.e("Hello,")
        job.join() // 等待直到子协程执行结束
    }

    fun main() = runBlocking { // 开始执行主协程
        val launch = GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            LogUtil.e("World!!!!!")
        }
        LogUtil.e("Hello,!!!!!") // 主协程在这里会立即执行
        delay(2000L)      // 延迟 2 秒来保证 JVM 存活

        launch.join()
    }

}