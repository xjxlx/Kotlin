package com.xjx.kotlin.ui.activity.test.xc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityXc5Binding

class Xc5Activity : AppBaseBindingTitleActivity<ActivityXc5Binding>() {

    override fun setTitleContent(): String {
        return "协程进阶"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityXc5Binding {
        return ActivityXc5Binding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

}