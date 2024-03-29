package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCustomProgressBinding

class CustomProgressActivity : BaseBindingTitleActivity<ActivityCustomProgressBinding>() {

	override fun getTitleContent(): String {
		return "综合评分"
	}

	override fun getBinding(
		inflater: LayoutInflater,
		container: ViewGroup?,
		attachToRoot: Boolean,
	): ActivityCustomProgressBinding {
		return ActivityCustomProgressBinding.inflate(inflater, container, true)
	}

	override fun initData(savedInstanceState: Bundle?) {
		mBinding.spvScore.init(this, 90)
		mBinding.btnStart.setOnClickListener {
			mBinding.spvScore.reset()
			var value = mBinding.etNumber.text.toString()
			if (TextUtils.isEmpty(value)) {
				value = "0"
			}
			val toFloat = value.toInt()
			mBinding.spvScore.setScore(toFloat, 23)
		}
	}
}
