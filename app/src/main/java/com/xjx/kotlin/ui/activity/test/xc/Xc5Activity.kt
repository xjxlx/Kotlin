package com.xjx.kotlin.ui.activity.test.xc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.common.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
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
        // runSingleCallBackInterface()

        LogUtil.e("job started")
        val job = lifecycleScope.launch {
            val singleCoroutine = runSingleCallBackCoroutine()
            LogUtil.e("singleCoroutine: $singleCoroutine")
        }
        LogUtil.e("job ---> cancel")
        // job.cancel()

        // more
        val job1 = lifecycleScope.launch {
            try {
                val moreCoroutine = moreCoroutine()
                LogUtil.e("moreCoroutine ->success: $moreCoroutine")
            } catch (e: Exception) {
                LogUtil.e("moreCoroutine ->failure: ${e}")
            }
        }

        // 携程结束的回调
        job1.invokeOnCompletion {
            LogUtil.e("job1: ---> invokeOnCompletion")
        }

//        LogUtil.e("job1: ---> cancel")
//        job1.cancel()
    }

    /**
     * 1：the usage of coroutine callbacks
     */
    private suspend fun runSingleCallBackCoroutine(): String {
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
                callback.onSuccess("success")
            } catch (e: Exception) {
                callback.onFailure(e)
            }
        }
    }

    /**
     *2: call the method
     */
    private suspend fun moreCoroutine(): String {
        return suspendCancellableCoroutine { cancellableContinuation ->
            request(object : ICallBack {
                override fun onSuccess(data: String) {
                    cancellableContinuation.resume(data)
                }

                override fun onFailure(t: Throwable) {
                    cancellableContinuation.resumeWithException(t)
                }
            })

            // 注册一个响应协程取消请求的回调函数，只有在取消的时候才会响应，如果正常结束了，则不会响应
            cancellableContinuation.invokeOnCancellation {
                // 取消
                LogUtil.e("-----> job -1 >>>> invokeOnCancellation")
            }
        }
    }
}