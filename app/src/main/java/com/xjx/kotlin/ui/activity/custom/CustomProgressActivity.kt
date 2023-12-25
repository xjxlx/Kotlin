package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCustomProgressBinding
import kotlinx.coroutines.launch

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

		mBinding.btnStart.setOnClickListener {
			mBinding.spvScore.reset()

			var value = mBinding.etNumber.text.toString()
			if (TextUtils.isEmpty(value)) {
				value = "0"
			}
			val toFloat = value.toInt()
			lifecycleScope.launch { mBinding.spvScore.setScore(this@CustomProgressActivity, toFloat, 23) }
		}
	}
}
