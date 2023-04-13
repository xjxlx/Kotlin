package com.xjx.kotlin.network

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.DownCountTime
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityNetWorkBinding
import com.xjx.kotlin.network.ApiLogic.UserLogic
import com.xjx.kotlin.network.bean.UserInfoBean
import com.xjx.kotlin.network.listener.HttpCallBackListener
import com.xjx.kotlin.ui.activity.test.Api
import com.xjx.kotlin.ui.activity.test.fx.TestFx1
import kotlinx.coroutines.launch

/**
 * 网络封装
 */
class NetWorkActivity : AppBaseBindingTitleActivity<ActivityNetWorkBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityNetWorkBinding {
        return ActivityNetWorkBinding.inflate(inflater, container, true)
    }

    override fun setTitleContent(): String {
        return "网络封装"
    }

    override fun initData(savedInstanceState: Bundle?) {
        val downCountTime = DownCountTime()
        downCountTime.setCountdown(5, 1000, object : DownCountTime.CallBack {
            override fun onTick(count: Long, current: Long) {
                LogUtil.e("DOWN--- ", "current: " + current + " count: " + count)
            }

            override fun onFinish() {
                LogUtil.e("DOWN--- ", "onFinish: ")
            }
        })

        lifecycleScope.launch {
            HttpClient.http(block = {
                UserLogic.getUser()
            }, listener = object : HttpCallBackListener<HttpResult<UserInfoBean>> {
                override fun onSuccess(t: HttpResult<UserInfoBean>) {

                }

                override fun onError(throwable: Throwable) {

                }
            })
        }

        mBinding.btnStart.setOnClickListener {
            downCountTime.start()
        }
        mBinding.btnPause.setOnClickListener {
            downCountTime.pause()
        }
        mBinding.btnResume.setOnClickListener {
            downCountTime.resume()
        }

        TestFx1.main(null)
    }

    inline fun <reified T, F, R> http(block: T.(F) -> HttpResult<R>, b: F): HttpResult<R> {
        val apiService = RetrofitHelper.create(T::class.java)
        apiService.block(b)
        return apiService.block(b)
    }

    inline fun <reified T, F, R> http2(block: T.(F) -> HttpResult<R>, b: F): HttpResult<R> {
        val apiService = RetrofitHelper.create(T::class.java)
        return apiService.block(b)
    }

    fun test() {
        val age = 1
        http<ApiService, Int, UserInfoBean>(
                { getUserInfo2(aaa()) }, 1)
    }

    //    T.()->Unit，(T) -> Unit，() -> Unit
    private fun <T, R, F> T.myApply(block: T.(F) -> HttpResult<R>?, b: F) {
        block(b)
    }

    private fun <T> T.myAlso(block: (T) -> Unit) {
        block(this)
    }

    private fun <T, R> T.apply2(block: T.(R) -> HttpResult<R>?, b: R) {
        block(b)
    }

    fun <T, U> myApply2(block: T.(U) -> HttpResult<U>, b: Int) {
    }

    fun <T, F> myApply3(block: T.(F) -> HttpResult<F>, b: F) {
    }

    suspend fun imp() {
        lifecycleScope.launch {
            val user2 = User()
            http<ApiService, Int, UserInfoBean>({
                getUserInfo2(9)
            }, 3)
        }
    }

    fun aaa(): Int {
        return 12
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val user = User()
//
//        // 此处该怎么调用？直接塞进去个
//        user.apply3<User, UserInfoBean>({
//            test2(1)
//        }, UserInfoBean())
//    }

    private fun <T, R> T.apply3(block: T.(R) -> HttpResult<R>?, b: R) {
        block(b)
    }

    class User {
        var name = "张三"
        fun test2(agt: Int): HttpResult<UserInfoBean> {
            return HttpResult()
        }
    }

    fun getResource(arg1: String, arg2: String, method: (arg01: String, arg02: String) -> String) {
        LogUtil.e(" 开始 执行方法 ----->")
        method(arg1, arg2)
        LogUtil.e(" 结束 执行方法 ----->")
    }

    fun action(arg1: String, arg2: String) :String{
        LogUtil.e("action : arg1: $arg1 arg2:$arg2")

        return "ssss"
    }

    inline fun <reified T, F, R> http3(block: T.(F) -> HttpResult<R>, b: F): HttpResult<R> {
        val apiService = RetrofitHelper.create(T::class.java)
        return apiService.block(b)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fun age(age:Int):Int{
            return age
        }
        getResource("111", "222", ::action)

    }



}