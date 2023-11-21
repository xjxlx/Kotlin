package com.xjx.kotlin.ui.activity.animations

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.android.common.utils.ToastUtil
import com.xjx.kotlin.databinding.ActivityAnimationJsonBinding

class AnimationJsonActivity : BaseBindingTitleActivity<ActivityAnimationJsonBinding>() {

    override fun getTitleContent(): String {
        return "JSON动画"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityAnimationJsonBinding {
        return ActivityAnimationJsonBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.bt1Start.setOnClickListener {
            mBinding.lav1.playAnimation()
        }

        mBinding.bt1Pause.setOnClickListener {
            mBinding.lav1.pauseAnimation()
        }

        mBinding.bt1Resume.setOnClickListener {
            mBinding.lav1.resumeAnimation()
        }

        mBinding.bt1Cancel.setOnClickListener {
            mBinding.lav1.cancelAnimation()
        }

        mBinding.bt1Stop.setOnClickListener {
            val animator = ValueAnimator.ofFloat(0f, 1f)
            animator.duration = 3000
            animator.addUpdateListener {
                //animationView.setProgress(animation.animatedValue)
                mBinding.lav1.progress = it.animatedValue as Float
            }
            animator.start()
        }



        mBinding.lav1.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                ToastUtil.show("动画开始")
            }

            override fun onAnimationEnd(animation: Animator) {
                ToastUtil.show("动画结束")
            }

            override fun onAnimationCancel(animation: Animator) {
                ToastUtil.show("动画取消")
            }

            override fun onAnimationRepeat(animation: Animator) {
                ToastUtil.show("动画重播")
            }
        })

        mBinding.lav1.addAnimatorPauseListener(object : Animator.AnimatorPauseListener {
            override fun onAnimationPause(animation: Animator) {
                ToastUtil.show("动画暂停")
            }

            override fun onAnimationResume(animation: Animator) {
                ToastUtil.show("动画重新开始")
            }
        })

        // 动画进度
        mBinding.lav1.addAnimatorUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                LogUtil.e("当前进度：${animation.animatedValue}")
            }
        })
    }
}