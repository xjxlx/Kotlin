package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.R
import com.xjx.kotlin.databinding.ActivityFunBinding

class FunActivity : AppBaseBindingTitleActivity<ActivityFunBinding>() {

    var a: String? = null
    var b: String = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fun)
//        ConvertDataActivity.aaa

        aaa
    }

    override fun initData(savedInstanceState: Bundle?) {
        val test5 = test5()
        LogUtil.e(test5.toString())

        // a 强转为不为空的类型，可能会报异常
        val length = a!!.length
        // a 如果为空，则后面不执行
        val length1 = a?.length

        // 多返回类型解构
        val (c, d, e) = test5()
        LogUtil.e("" + c)
        LogUtil.e("" + d)
        LogUtil.e("" + e)

        val tzFun = test1.tzFun("abc")
        LogUtil.e(tzFun)

        var tz = TZ()
        tz.text1()
        tz.getParams("")
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

    private var test1 = "Hello Word"

    // 拓展方法
    fun String.tzFun(par: String): String {
        return "$par--->"
    }

    // 拓展方法
    fun TZ.getParams(pra: String): String {
        return "$pra --->"
    }

    // 普通类
    class TZ {
        fun text1(): String {
            return ""
        }
    }
}