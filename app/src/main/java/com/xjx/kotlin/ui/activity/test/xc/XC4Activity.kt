package com.xjx.kotlin.ui.activity.test.xc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityXc4Binding

class XC4Activity : AppBaseBindingTitleActivity<ActivityXc4Binding>() {

    override fun setTitleContent(): String {
        return "协程 - 4 - 管道"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityXc4Binding {
        return ActivityXc4Binding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

}