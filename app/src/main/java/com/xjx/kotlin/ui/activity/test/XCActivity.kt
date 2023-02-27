package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityXcactivityBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class XCActivity : AppBaseBindingTitleActivity<ActivityXcactivityBinding>() {

    override fun setTitleContent(): String {
        return "协程"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityXcactivityBinding {
        return ActivityXcactivityBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

        GlobalScope.launch(Dispatchers.Main) {
//            mBinding.tvTest1.text = "我是协程1"
            delay(1000)
            LogUtil.e("我是协程1 :" + Thread.currentThread().name)
        }

        GlobalScope.launch {
            delay(2000)
            LogUtil.e("我是协程2 :" + Thread.currentThread().name)
//            mBinding.tvTest2.text = "我是协程2"
        }

        GlobalScope.launch(Dispatchers.IO) {
            delay(3000)
            LogUtil.e("我是协程3 :" + Thread.currentThread().name)
//            mBinding.tvTest3.text = "我是协程3"
        }
    }

}