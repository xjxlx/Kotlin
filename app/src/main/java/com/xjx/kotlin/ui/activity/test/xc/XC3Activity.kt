package com.xjx.kotlin.ui.activity.test.xc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityXc3Binding

class XC3Activity : AppBaseBindingTitleActivity<ActivityXc3Binding>() {

    override fun setTitleContent(): String {
        return "协程 - 3 - 流"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityXc3Binding {
        return ActivityXc3Binding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

}