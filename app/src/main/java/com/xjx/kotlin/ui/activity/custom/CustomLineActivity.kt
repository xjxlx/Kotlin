package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCustomLineBinding

class CustomLineActivity : BaseBindingTitleActivity<ActivityCustomLineBinding>() {

	override fun getTitleContent(): String {
		return "测试自定义Line"
	}

	override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityCustomLineBinding {
		return ActivityCustomLineBinding.inflate(inflater, container, attachToRoot)
	}

	override fun initData(savedInstanceState: Bundle?) {
		mBinding.btnFl1.setOnClickListener {
			mBinding.dl.animator()
		}
		mBinding.btnFl2.setOnClickListener {
			mBinding.dl.ch1f()
		}
	}
}
