package com.xjx.kotlin.ui.activity.test.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityFlowMapBinding

class FlowMapActivity : AppBaseBindingTitleActivity<ActivityFlowMapBinding>() {

    override fun setTitleContent(): String {
        return "Flow 集合"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityFlowMapBinding {
        return ActivityFlowMapBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.tvItemFlowF.setOnClickListener {
            startActivity(FlowControlActivity::class.java)
        }
    }
}