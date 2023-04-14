package com.xjx.kotlin.ui.activity.test.xc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityXc5Binding
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * suspendCoroutine： 是一个挂起函数，可以在携程内把结果通过resume方法传递给Coroutine中，这样Coroutine就具有返回值了
 *      作用就是把单一接口的方法改造成具有返回值的方法
 *
 */
class Xc5Activity : AppBaseBindingTitleActivity<ActivityXc5Binding>() {

    override fun setTitleContent(): String {
        return "协程进阶"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityXc5Binding {
        return ActivityXc5Binding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        runSingleCallBackInterface()

        lifecycleScope.launch {
            val singleCoroutine = runSingleCallBackCoroutine()
            LogUtil.e("singleCoroutine: $singleCoroutine")

            try {
                val moreCoroutine = moreCoroutine()
                LogUtil.e("moreCoroutine ->success: $moreCoroutine")
            } catch (e: Exception) {
                LogUtil.e("moreCoroutine ->failure: ${e.message}")
            }
        }
    }

    /**
     * 1：the usage of coroutine callbacks
     */
    private suspend fun runSingleCallBackCoroutine(): String {
        // suspendCoroutine是一个挂起函数
        return suspendCoroutine { continuation ->
            runTask(object : SingleMethodCallback {
                override fun onCallBack(value: String) {
                    // send the result
                    continuation.resume(value)
                }
            })
        }
    }

    /**
     * 1：the usage of interface callbacks
     */
    private fun runSingleCallBackInterface() {
        runTask(object : SingleMethodCallback {
            override fun onCallBack(value: String) {
                LogUtil.e(" method is called ")
            }
        })
    }

    /**
     * 1：模拟一个耗时操作
     */
    private fun runTask(callback: SingleMethodCallback) {
        thread {
            Thread.sleep(3000)
            callback.onCallBack("result")
        }
    }

    /**
     * 1:single callback interface
     */
    interface SingleMethodCallback {
        fun onCallBack(value: String)
    }

    /**
     * 2：success and  failure
     */
    interface ICallBack {
        fun onSuccess(data: String)
        fun onFailure(t: Throwable)
    }

    /**
     * 2：模拟一个耗时操作
     */
    private fun request(callback: ICallBack) {
        thread {
            try {
                Thread.sleep(3000)
//                val ints = arrayOf(3)
//                LogUtil.e(ints[99])
                callback.onSuccess("success")
            } catch (e: Exception) {
                callback.onFailure(e)
            }
        }
    }

    /**
     *2: call the method
     */
    private suspend fun moreCoroutine(): Any {
        return suspendCancellableCoroutine { cancellableContinuation ->
            request(object : ICallBack {
                override fun onSuccess(data: String) {
                    cancellableContinuation.resume(data)
                }

                override fun onFailure(t: Throwable) {
                    cancellableContinuation.resumeWithException(t)
                }
            })
        }
    }

}