package com.xjx.kotlin.ui.activity.test

import com.android.helper.utils.LogUtil

/**
 * @author : 流星
 * @CreateDate: 2022/7/24-23:09
 * @Description:
 */
class Person(var x: String) : InterfaceClass {

    var a: Int = 0

    // 副构造器，必须要调用主构造器
    constructor(x: Int, y: String) : this(y) {
        a = x
    }

    fun test1(p: String) {
        LogUtil.e(p)
    }

    override fun show(p: String) {

    }

}