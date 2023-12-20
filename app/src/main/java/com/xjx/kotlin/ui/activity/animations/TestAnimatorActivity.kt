package com.xjx.kotlin.ui.activity.animations

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityTestAnimatorBinding

class TestAnimatorActivity : BaseBindingTitleActivity<ActivityTestAnimatorBinding>() {

	private val animator = ValueAnimator.ofFloat(0F, 1F).also {
		it.duration = 2000
		it.addListener(object : Animator.AnimatorListener {
			override fun onAnimationStart(animation: Animator) {
				LogUtil.e("onAnimationStart")
			}

			override fun onAnimationEnd(animation: Animator) {
				LogUtil.e("onAnimationEnd")
			}

			override fun onAnimationCancel(animation: Animator) {
				LogUtil.e("onAnimationCancel")
			}

			override fun onAnimationRepeat(animation: Animator) {
				LogUtil.e("onAnimationRepeat")
			}
		})
	}

	override fun getTitleContent(): String {
		return "测试动画"
	}

	override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTestAnimatorBinding {
		return ActivityTestAnimatorBinding.inflate(inflater, container, attachToRoot)
	}

	override fun initData(savedInstanceState: Bundle?) {
		mBinding.btnStart.setOnClickListener {
			animator.start()
		}
		mBinding.btnFz.setOnClickListener {
			animator.reverse()
		}
	}
}
