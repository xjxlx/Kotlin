package com.xjx.kotlin.ui.activity.test.xc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityXc4Binding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch

class XC4Activity : AppBaseBindingTitleActivity<ActivityXc4Binding>() {

    override fun setTitleContent(): String {
        return "协程 - 4 - 管道"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityXc4Binding {
        return ActivityXc4Binding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val channel = Channel<Int>(1)

        val rendezvousChannel = Channel<String>()
        val bufferedChannel = Channel<String>(10)
        val conflatedChannel = Channel<String>(CONFLATED)
        val unlimitedChannel = Channel<String>(UNLIMITED)

        GlobalScope.launch {

//            launch {
//                // 这里可能是消耗大量 CPU 运算的异步逻辑，我们将仅仅做 5 次整数的平方并发送
//                for (x in 1..5) {
//                    channel.send(x * x)
//                }
//            }
//
//            // 这里我们打印了 5 次被接收的整数：
//            launch {
//                repeat(5) {
//                  println(channel.receive())
//                }
//                println("Done!")
//            }

            // test  iteration
            // 这里可能是消耗大量 CPU 运算的异步逻辑，我们将仅仅做 5 次整数的平方并发送
//            GlobalScope.launch {
//                for (x in 1..5) {
//                    channel.send(x * x)
//                }
//                channel.close()
//            }

//            GlobalScope.launch {
//                val iterator = channel.iterator()
//                while (iterator.hasNext()) {
//                    val next = iterator.next()
//                    LogUtil.e(next)
//                }
//                LogUtil.e("Done!")

//                for (item in channel) {
//                    LogUtil.e(item)
//                }
//                LogUtil.e("Done!")
//            }

            // test close
//            launch {
//                // 这里可能是消耗大量 CPU 运算的异步逻辑，我们将仅仅做 5 次整数的平方并发送
//                for (x in 1..5){
//                    channel.send(x * x)
//                }
//                // 结束发送，并停止阻塞
//                channel.close()
//            }
//
//            for (y in channel) {
//                LogUtil.e(y)
//            }
//            LogUtil.e("Done!")

            // 测试生产者和消费者
        }
    }

}