package com.xjx.kotlin.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.httpclient.error.ApiServices
import com.android.helper.utils.DownCountTime
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityNetWorkBinding
import com.xjx.kotlin.network.ApiLogic.UserLogic
import com.xjx.kotlin.network.bean.UserInfoBean
import com.xjx.kotlin.network.listener.HttpCallBackListener
import com.xjx.kotlin.ui.activity.test.aaa
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

    suspend inline fun <reified T, F, R> http(block: T.(F) -> HttpResult<R>, b: F): HttpResult<R> {
        val apiService = RetrofitHelper.create(T::class.java)
        apiService.block(b)
        return apiService.block(b)
    }

    //    T.()->Unit，(T) -> Unit，() -> Unit
    private fun <T, R, F> T.myApply(block: T.(F) -> HttpResult<R>?, b: F) {
        block(b)
    }

    private fun <T> T.myAlso(block: (T) -> Unit) {
        block(this)
    }

    private fun <T, R> T.myApply2(block: T.(R) -> HttpResult<R>?, b: R) {
        block(b)
    }

    fun <T, F> myApply3(block: T.(F) -> HttpResult<F>, b: F) {

    }

    suspend fun imp() {
        lifecycleScope.launch {
            val user2 = User()


//            http<ApiService, Nothing, UserInfoBean>({
//                getUserInfo()
//            },"")

//            http<ApiService, Int, UserInfoBean>({
//                getUserInfo2(2)
//            }, 2)

            http<ApiService, Int, UserInfoBean>({
                getUserInfo2(9)
            }, 3)

            val user = User()
            user.myApply<User, UserInfoBean, Int>(
                    { test2(3) },
                    2)

            user.myAlso {
                it.name = "王五"
            }
        }
    }

    fun aaa(age: Int): Int {
        return age
    }

    class User {
        var name = "张三"

        fun test(): HttpResult<UserInfoBean>? {
            return null
        }

        fun test2(age: Int): HttpResult<UserInfoBean>? {
            return null
        }

    }

}