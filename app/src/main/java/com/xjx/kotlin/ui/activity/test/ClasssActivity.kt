package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityClasssBinding
import java.util.*

class ClasssActivity : AppBaseBindingTitleActivity<ActivityClasssBinding>() {

    var a: Int = 0

    override fun initData(savedInstanceState: Bundle?) {
        val person = Person(1, "2")
        LogUtil.e("" + person.x)
        person.test1("测试哈哈！")

        person.show("222")

        // 引用
        val kMutableProperty1 = Person::a
        LogUtil.e("" + kMutableProperty1)
        person.let {
            it.a

        }
    }

    override fun setTitleContent(): String {
        return "类"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityClasssBinding {
        return ActivityClasssBinding.inflate(inflater, container, true)
    }
}