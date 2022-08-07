package com.xjx.kotlin.ui.activity.test

import com.android.helper.utils.LogUtil

/**
 * @author : 流星
 * @CreateDate: 2022/7/24-23:46
 * @Description:
 */
open class AbstractClassImp : AbstractClass() {

    // 重写抽象方法
    override fun absFun() {
    }

    // 重写声明的方法
    override fun test2() {
        super.test2()
        val person = Person(1, "2")
        person.a

        // 引用
        val kMutableProperty1 = Person::a
        LogUtil.e("" + kMutableProperty1)

    }
}

class AAA : AbstractClassImp() {

}