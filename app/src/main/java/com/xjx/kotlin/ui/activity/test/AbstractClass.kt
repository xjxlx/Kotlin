package com.xjx.kotlin.ui.activity.test

import com.android.common.utils.LogUtil

/**
 * @author : 流星
 * @CreateDate: 2022/7/24-23:44
 * @Description:
 */
abstract class AbstractClass {

    // 不可复写
    fun test() {
        LogUtil.e("我是普通方法！")
    }

    // 加入open字段，标注可以被复写
    open fun test2() {
        LogUtil.e("可以被复写了！")
    }

    // 定义抽象方法
    abstract fun absFun();

}