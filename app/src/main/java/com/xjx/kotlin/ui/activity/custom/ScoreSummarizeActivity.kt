package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.interfaces.AnimationListener
import com.xjx.kotlin.databinding.ActivityScoreSummarizeBinding

class ScoreSummarizeActivity : BaseBindingTitleActivity<ActivityScoreSummarizeBinding>() {
    override fun getTitleContent(): String {
        return "分数综合"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?,
        attachToRoot: Boolean): ActivityScoreSummarizeBinding {
        return ActivityScoreSummarizeBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.spvScore.init(this, 10)
        mBinding.cvChart.init(this)
        mBinding.twvWrap.init(this)

        mBinding.btnStart.setOnClickListener {
            mBinding.spvScore.reset()
            mBinding.twvWrap.reset()
            val bottomArray = floatArrayOf(0.6f, 0.3f, 0.2f, 0.1f, 0.4f)
            val topArray = floatArrayOf(1f, 0.3f, 0.5f, 0.7f, 1f)
            val scoreArray = intArrayOf(20, 30, 50, 20, 100)
            mBinding.cvChart.setChartArray(bottomArray, topArray, scoreArray)

            mBinding.cvChart.setAnimationListener(object : AnimationListener {
                override fun onEndAnimation() {
                    mBinding.spvScore.setScore(13, 41)
                    mBinding.spvScore.setAnimationListener(object : AnimationListener {
                        override fun onEndAnimation() {
                            mBinding.twvWrap.setExplain("加油",
                                "情绪稳定，心率不错，注意呼吸节奏，呼气更缓慢持久、吸气更平稳绵长、呼吸转换时适当憋气效果更好哦！吸转换时适当憋气效果更好哦！")
                        }
                    })
                }
            })

            this.getExternalFilesDir("")
        }
    }
}
