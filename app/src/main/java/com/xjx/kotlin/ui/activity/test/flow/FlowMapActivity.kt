package com.xjx.kotlin.ui.activity.test.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.R
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
        setonClickListener(R.id.tv_item_flow, R.id.tv_item_flow_call)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {

            R.id.tv_item_flow -> {
                startActivity(FlowActivity::class.java)
            }

            R.id.tv_item_flow_call -> {
                startActivity(FlowCallActivity::class.java)
            }
        }
    }
}