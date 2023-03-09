package com.xjx.kotlin.ui.activity.test.xc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityXc2Binding

class XC2Activity : AppBaseBindingTitleActivity<ActivityXc2Binding>() {

    override fun setTitleContent(): String {
        return "协程 - 2"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityXc2Binding {
        return ActivityXc2Binding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}