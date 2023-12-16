package com.xjx.kotlin.ui.activity.test.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.R
import com.xjx.kotlin.databinding.ActivityFlowMapBinding

class FlowMapActivity : BaseBindingTitleActivity<ActivityFlowMapBinding>() {

	override fun getTitleContent(): String {
		return "Flow 集合"
	}

	override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityFlowMapBinding {
		return ActivityFlowMapBinding.inflate(inflater, container, true)
	}

	override fun initData(savedInstanceState: Bundle?) {
		mBinding.tvItemFlowF.setOnClickListener { startActivity(FlowControlActivity::class.java) }
		setonClickListener(R.id.tv_item_flow, R.id.tv_item_flow_call)

		mBinding.tvItemTestMutableFlow.setOnClickListener {
			startActivity(TestMutableFlowActivity::class.java)
		}
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
