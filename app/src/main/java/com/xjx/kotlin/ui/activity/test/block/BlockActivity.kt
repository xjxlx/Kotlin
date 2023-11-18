package com.xjx.kotlin.ui.activity.test.block

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityBlockBinding

class BlockActivity : BaseBindingTitleActivity<ActivityBlockBinding>() {

    override fun getTitleContent(): String {
        return "Block 语法"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityBlockBinding {
        return ActivityBlockBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        // 无参 无返回
        testBlockNoArguments { LogUtil.e("it ----> " + it) }

        test4 { x, y ->
            return@test4 x + y
        }

        test6(1, 2, ::test7)
    }

    private fun testBlockNoArguments(block: (arg: String) -> Unit) {
        LogUtil.e("无参 ---> 无返回 ！")
        block("abc")
    }

    // kotlin
    private fun test4(block: (x: Int, y: Int) -> Int) {
        val result = block(1, 2)
        LogUtil.e("result :$result")
    }

    private fun test5(block: (x: Int, y: Int) -> Unit) {
        block(1, 2)
    }

    // 把方法作为参数使用
    fun test6(a: Int, b: Int, block: (x: Int, y: Int) -> Unit) {
        block(1, 2)
    }

    fun test7(x: Int, y: Int) {
        LogUtil.e("x:$x y  :$y")
    }

    class Test1 {
        fun isOdd(x: Int) = x % 2 != 0

        fun test() {
            var list = listOf(1, 2, 3, 4, 5)
            print(list.filter(::isOdd))
        }
    }
}
