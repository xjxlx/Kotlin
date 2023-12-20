package com.xjx.kotlin.ui.activity.animations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityAnimationMapBinding

class AnimationMapActivity : BaseBindingTitleActivity<ActivityAnimationMapBinding>() {

	override fun getTitleContent(): String {
		return "动画集合"
	}

	override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityAnimationMapBinding {
		return ActivityAnimationMapBinding.inflate(inflater, container, true)
	}

	override fun initListener() {
		super.initListener()
		mBinding.tvJson.setOnClickListener {
			startActivity(AnimationJsonActivity::class.java)
		}
		mBinding.tvTestAnimator.setOnClickListener {
			startActivity(TestAnimatorActivity::class.java)
		}
	}

	override fun initData(savedInstanceState: Bundle?) {
	}
}
