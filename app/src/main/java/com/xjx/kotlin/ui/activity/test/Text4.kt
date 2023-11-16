package com.xjx.kotlin.ui.activity.test

import com.android.common.utils.LogUtil

/**
 * @author : 流星
 * @CreateDate: 2022/7/26-7:39
 * @Description:
 */
class Text4 {

    fun test3() {
        LogUtil.e(a)
    }

    var e: String? = null
    var s1: StringBuffer? = null
    var s2: StringBuilder? = null

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            test1()
        }

        fun test1() {
            LogUtil.e(a)
            test12()
        }

        fun test12() {
            LogUtil.e(a)
        }

        const val a = "1"
        const val B = "1"
        var c = "1"
        var d: String? = null
    }
}