package com.xjx.kotlin.ui.activity.test.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityFlowControlBinding

class FlowControlActivity : AppBaseBindingTitleActivity<ActivityFlowControlBinding>() {

    override fun setTitleContent(): String {
        return "Flow 操作符"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityFlowControlBinding {
        return ActivityFlowControlBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}