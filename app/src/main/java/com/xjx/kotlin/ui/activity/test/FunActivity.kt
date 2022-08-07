package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.R
import com.xjx.kotlin.databinding.ActivityFunBinding

class FunActivity : AppBaseBindingTitleActivity<ActivityFunBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fun)
    }

    override fun initData(savedInstanceState: Bundle?) {

        val (a, b) = text4(1)

        val test5 = test5()
        LogUtil.e(test5.toString())

        // 多返回类型解构
        val (c, d, e) = test5()
        LogUtil.e("" + c)
        LogUtil.e("" + d)
        LogUtil.e("" + e)

    }

    override fun setTitleContent(): String {
        return "函数"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityFunBinding {
        return ActivityFunBinding.inflate(inflater, container, true)
    }

    // 无返回值的函数
    fun test(key: String): Unit {

    }

    // 指定返回值类型
    fun test1(key: Int): Int {
        return 1
    }

    // 任意类型的函数
    fun test2(): Any {
        return false
    }

    // 可变参数
    fun test3(vararg params: Int) {
        // params:是一个数组
    }

    // 两种返回类型函数
    private fun text4(key: Int): Pair<Int, String> {
        return Pair(1, "哈哈")
    }

    // 三种返回值类型
    private fun test5(): Triple<Int, String, Boolean> {
        return Triple(2, "张三", false)
    }

}