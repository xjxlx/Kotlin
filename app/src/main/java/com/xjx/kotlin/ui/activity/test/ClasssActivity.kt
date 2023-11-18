package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityClasssBinding

class ClasssActivity : BaseBindingTitleActivity<ActivityClasssBinding>() {

    var a: Int = 0

    override fun initData(savedInstanceState: Bundle?) {
        val person = Person(1, "2")
        LogUtil.e("" + person.x)
        person.test1("测试哈哈！")

        person.show("222")

        // region 引用
        val kMutableProperty1 = Person::a
        LogUtil.e("" + kMutableProperty1)
        person.let { it.a }
        // endregion
    }

    override fun getTitleContent(): String {
        return "类"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityClasssBinding {
        return ActivityClasssBinding.inflate(inflater, container, true)
    }
}

class A(var a: Int, val b: String, c: Boolean) {
    // a 和 b 是全局可见的参数
    // c 是构造方法和 init块内可见
}

interface Api {
    fun a()
    fun b()
    fun c()
}

// 必须实现
class ImpApi : Api {
    override fun a() {}

    override fun b() {}

    override fun c() {}
}

// 必须实现
class ApiWrapper(val api: Api) : Api {
    override fun a() {
        api.a()
    }

    override fun b() {
        api.b()
    }

    override fun c() {
        api.c()
    }
}

// 代理实现，让参数 api 去代理对象Api去实现自己需要的事情
class ApiWrapper2(private val api: Api) : Api by api {
    override fun a() {
        api.a()
    }
}
