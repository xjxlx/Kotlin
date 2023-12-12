package com.xjx.kotlin.ui.activity.animations

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.android.common.utils.ToastUtil
import com.xjx.kotlin.databinding.ActivityAnimationJsonBinding

class AnimationJsonActivity : BaseBindingTitleActivity<ActivityAnimationJsonBinding>() {

    /**
     * 1: breath in
     * 2: breath in h
     * 3: breath out
     * 4: breath out h
     */
    private var ANIMATION_TYPE = 0

    override fun getTitleContent(): String {
        return "JSON动画"
    }

    override fun initData(savedInstanceState: Bundle?) {
        /** 开始 **/
        mBinding.bt1Start.setOnClickListener {
            // mBinding.lav1.setAnimation(R.raw.breath_in)
            mBinding.lav1.visibility = View.VISIBLE
            mBinding.lav2.visibility = View.INVISIBLE
            mBinding.lav3.visibility = View.INVISIBLE
            mBinding.lav4.visibility = View.INVISIBLE
            mBinding.lav5.visibility = View.INVISIBLE
            mBinding.lav6.visibility = View.INVISIBLE

            mBinding.lav1.playAnimation()

            ANIMATION_TYPE = 1
        }

        /** 暂停 **/
        mBinding.bt1Pause.setOnClickListener {
            mBinding.lav1.pauseAnimation()
        }

        /** 重新开始 **/
        mBinding.bt1Resume.setOnClickListener {
            mBinding.lav1.resumeAnimation()
        }

        /** 取消 **/
        mBinding.bt1Cancel.setOnClickListener {
            cancelAll()
        }

        /** 监听 暂停 / 恢复 **/
        mBinding.lav1.addAnimatorPauseListener(object : Animator.AnimatorPauseListener {
            override fun onAnimationPause(animation: Animator) {
                ToastUtil.show("动画暂停")
            }

            override fun onAnimationResume(animation: Animator) {
                ToastUtil.show("动画重新开始")
            }
        })

        /** 动画进度 **/
        mBinding.lav1.addAnimatorUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                LogUtil.e("当前进度：${animation.animatedValue}")
            }
        })

        /** 全部呼吸法 **/
        mBinding.bt1AnimationAll.setOnClickListener {
            cancelAll()
            defaultAnimationListener()
        }

        /** 风箱呼吸阀 **/
        mBinding.bt1AnimationFx.setOnClickListener {
            cancelAll()
            AnimationListener1()
        }

        /** 清理脉络 **/
        mBinding.bt1AnimationQlml.setOnClickListener {
            cancelAll()
            animationListener2()
        }

        /** 圣光 **/
        mBinding.bt1AnimationSg.setOnClickListener {
            cancelAll()
            animationListener3()
        }

        /** 倒计时 **/
        mBinding.bt1AnimationDjs.setOnClickListener {
            animationListener4()
        }

        /** 单个动画 **/
        mBinding.bt1AnimationSingle.setOnClickListener {
            mBinding.lav1.visibility = View.INVISIBLE
            mBinding.lav2.visibility = View.INVISIBLE
            mBinding.lav3.visibility = View.INVISIBLE
            mBinding.lav4.visibility = View.INVISIBLE
            mBinding.lav5.visibility = View.INVISIBLE
            mBinding.lav6.visibility = View.VISIBLE
            mBinding.lav6.playAnimation()
            mBinding.lav61.playAnimation()
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityAnimationJsonBinding {
        return ActivityAnimationJsonBinding.inflate(inflater, container, true)
    }

    private fun cancelAll() {
        mBinding.lav1.cancelAnimation()
        mBinding.lav2.cancelAnimation()
        mBinding.lav3.cancelAnimation()
        mBinding.lav4.cancelAnimation()
        mBinding.lav5.cancelAnimation()
        mBinding.lav6.cancelAnimation()
    }

    /**
     * 普通呼吸法
     */
    fun defaultAnimationListener() {
        // mBinding.lav1.setAnimation(R.raw.breath_in)
        mBinding.lav1.visibility = View.VISIBLE
        mBinding.lav2.visibility = View.INVISIBLE
        mBinding.lav3.visibility = View.INVISIBLE
        mBinding.lav4.visibility = View.INVISIBLE
        mBinding.lav5.visibility = View.INVISIBLE
        mBinding.lav6.visibility = View.INVISIBLE
        mBinding.lav1.playAnimation()

        mBinding.lav1.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                ToastUtil.show("播放吸气动画")
            }

            override fun onAnimationEnd(animation: Animator) {
                ToastUtil.show("播放吸气屏气动画")
                mBinding.lav1.visibility = View.INVISIBLE
                mBinding.lav2.visibility = View.VISIBLE
                mBinding.lav3.visibility = View.INVISIBLE
                mBinding.lav4.visibility = View.INVISIBLE
                mBinding.lav5.visibility = View.INVISIBLE
                mBinding.lav6.visibility = View.INVISIBLE
                mBinding.lav2.playAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        mBinding.lav2.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                ToastUtil.show("播放呼气动画")

                mBinding.lav1.visibility = View.INVISIBLE
                mBinding.lav2.visibility = View.INVISIBLE
                mBinding.lav3.visibility = View.VISIBLE
                mBinding.lav4.visibility = View.INVISIBLE
                mBinding.lav5.visibility = View.INVISIBLE
                mBinding.lav6.visibility = View.INVISIBLE
                mBinding.lav3.playAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        mBinding.lav3.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                ToastUtil.show("播放呼气屏气动画")
                mBinding.lav1.visibility = View.INVISIBLE
                mBinding.lav2.visibility = View.INVISIBLE
                mBinding.lav3.visibility = View.INVISIBLE
                mBinding.lav4.visibility = View.VISIBLE
                mBinding.lav5.visibility = View.INVISIBLE
                mBinding.lav6.visibility = View.INVISIBLE
                mBinding.lav4.playAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        mBinding.lav4.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                mBinding.lav1.visibility = View.VISIBLE
                mBinding.lav2.visibility = View.INVISIBLE
                mBinding.lav3.visibility = View.INVISIBLE
                mBinding.lav4.visibility = View.INVISIBLE
                mBinding.lav5.visibility = View.INVISIBLE
                mBinding.lav6.visibility = View.INVISIBLE

                mBinding.lav1.playAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }

    /**
     * 风箱呼吸法
     */
    fun AnimationListener1() {
        mBinding.lav1.visibility = View.VISIBLE
        mBinding.lav2.visibility = View.INVISIBLE
        mBinding.lav3.visibility = View.INVISIBLE
        mBinding.lav4.visibility = View.INVISIBLE
        mBinding.lav5.visibility = View.INVISIBLE
        mBinding.lav6.visibility = View.INVISIBLE
        mBinding.lav1.playAnimation()

        mBinding.lav1.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                ToastUtil.show("播放吸气动画")
            }

            override fun onAnimationEnd(animation: Animator) {
                ToastUtil.show("播放吸气屏气动画")
                mBinding.lav1.visibility = View.INVISIBLE
                mBinding.lav2.visibility = View.INVISIBLE
                mBinding.lav3.visibility = View.VISIBLE
                mBinding.lav4.visibility = View.INVISIBLE
                mBinding.lav5.visibility = View.INVISIBLE
                mBinding.lav6.visibility = View.INVISIBLE
                mBinding.lav3.playAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
//        mBinding.lav2.addAnimatorListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator) {
//            }
//
//            override fun onAnimationEnd(animation: Animator) {
//                ToastUtil.show("播放呼气动画")
//
//                mBinding.lav1.visibility = View.INVISIBLE
//                mBinding.lav2.visibility = View.INVISIBLE
//                mBinding.lav3.visibility = View.VISIBLE
//                mBinding.lav4.visibility = View.INVISIBLE
//                mBinding.lav3.playAnimation()
//            }
//
//            override fun onAnimationCancel(animation: Animator) {
//            }
//
//            override fun onAnimationRepeat(animation: Animator) {
//            }
//        })
        mBinding.lav3.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                ToastUtil.show("播放呼气屏气动画")
                mBinding.lav1.visibility = View.VISIBLE
                mBinding.lav2.visibility = View.INVISIBLE
                mBinding.lav3.visibility = View.INVISIBLE
                mBinding.lav4.visibility = View.INVISIBLE
                mBinding.lav5.visibility = View.INVISIBLE
                mBinding.lav6.visibility = View.INVISIBLE
                mBinding.lav1.playAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
//        mBinding.lav4.addAnimatorListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator) {
//            }
//
//            override fun onAnimationEnd(animation: Animator) {
//                mBinding.lav1.visibility = View.VISIBLE
//                mBinding.lav2.visibility = View.INVISIBLE
//                mBinding.lav3.visibility = View.INVISIBLE
//                mBinding.lav4.visibility = View.INVISIBLE
//
//                mBinding.lav1.playAnimation()
//            }
//
//            override fun onAnimationCancel(animation: Animator) {
//            }
//
//            override fun onAnimationRepeat(animation: Animator) {
//            }
//        })
    }

    /**
     * 清理脉络
     */
    fun animationListener2() {
        mBinding.lav1.visibility = View.VISIBLE
        mBinding.lav2.visibility = View.INVISIBLE
        mBinding.lav3.visibility = View.INVISIBLE
        mBinding.lav4.visibility = View.INVISIBLE
        mBinding.lav5.visibility = View.INVISIBLE
        mBinding.lav6.visibility = View.INVISIBLE
        mBinding.lav1.playAnimation()

        mBinding.lav1.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                ToastUtil.show("播放吸气动画")
            }

            override fun onAnimationEnd(animation: Animator) {
                ToastUtil.show("播放吸气屏气动画")
                mBinding.lav1.visibility = View.INVISIBLE
                mBinding.lav2.visibility = View.VISIBLE
                mBinding.lav3.visibility = View.INVISIBLE
                mBinding.lav4.visibility = View.INVISIBLE
                mBinding.lav5.visibility = View.INVISIBLE
                mBinding.lav6.visibility = View.INVISIBLE
                mBinding.lav2.playAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        mBinding.lav2.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                ToastUtil.show("播放呼气动画")

                mBinding.lav1.visibility = View.INVISIBLE
                mBinding.lav2.visibility = View.INVISIBLE
                mBinding.lav3.visibility = View.VISIBLE
                mBinding.lav4.visibility = View.INVISIBLE
                mBinding.lav5.visibility = View.INVISIBLE
                mBinding.lav6.visibility = View.INVISIBLE
                mBinding.lav3.playAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        mBinding.lav3.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                ToastUtil.show("播放呼气屏气动画")
                mBinding.lav1.visibility = View.VISIBLE
                mBinding.lav2.visibility = View.INVISIBLE
                mBinding.lav3.visibility = View.INVISIBLE
                mBinding.lav4.visibility = View.INVISIBLE
                mBinding.lav5.visibility = View.INVISIBLE
                mBinding.lav6.visibility = View.INVISIBLE
                mBinding.lav1.playAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }

    /**
     * 圣光
     */
    fun animationListener3() {
        mBinding.lav1.visibility = View.VISIBLE
        mBinding.lav2.visibility = View.INVISIBLE
        mBinding.lav3.visibility = View.INVISIBLE
        mBinding.lav4.visibility = View.INVISIBLE
        mBinding.lav5.visibility = View.INVISIBLE
        mBinding.lav6.visibility = View.INVISIBLE
        mBinding.lav1.playAnimation()

        mBinding.lav1.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                ToastUtil.show("播放吸气动画")
            }

            override fun onAnimationEnd(animation: Animator) {
                ToastUtil.show("播放吸气屏气动画")
//                mBinding.lav1.visibility = View.INVISIBLE
//                mBinding.lav2.visibility = View.VISIBLE
//                mBinding.lav3.visibility = View.INVISIBLE
//                mBinding.lav4.visibility = View.INVISIBLE
//                mBinding.lav2.playAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }

    /**
     * 倒计时
     */
    fun animationListener4() {
        mBinding.lav1.visibility = View.INVISIBLE
        mBinding.lav2.visibility = View.INVISIBLE
        mBinding.lav3.visibility = View.INVISIBLE
        mBinding.lav4.visibility = View.INVISIBLE
        mBinding.lav5.visibility = View.VISIBLE
        mBinding.lav6.visibility = View.INVISIBLE
        mBinding.lav5.playAnimation()

        mBinding.lav1.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                ToastUtil.show("播放倒计时动画")
//                mBinding.lav1.visibility = View.INVISIBLE
//                mBinding.lav2.visibility = View.VISIBLE
//                mBinding.lav3.visibility = View.INVISIBLE
//                mBinding.lav4.visibility = View.INVISIBLE
//                mBinding.lav2.playAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelAll()
        mBinding.lav6.cancelAnimation()
    }
}