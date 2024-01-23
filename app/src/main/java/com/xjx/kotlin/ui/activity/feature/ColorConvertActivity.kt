package com.xjx.kotlin.ui.activity.feature

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityColorConvertBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch

class ColorConvertActivity : BaseBindingTitleActivity<ActivityColorConvertBinding>() {

    private val mFlow: MutableStateFlow<Int> by lazy {
        return@lazy MutableStateFlow(0)
    }

    override fun getTitleContent(): String {
        return "测试颜色转换"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityColorConvertBinding {
        return ActivityColorConvertBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initData(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            mFlow.sample(100).collect {
                LogUtil.e("color-2", "color - 2 :${it}")
                mBinding.vBg.setBackgroundColor(it)
            }
        }

        mBinding.btnStart.setOnClickListener {
            var temp = 0
            val animator = ValueAnimator.ofArgb(Color.RED, Color.BLUE)
            animator.duration = 1000
            animator.addUpdateListener {
                val value = it.animatedValue as Int
                if (value != temp) {
                    LogUtil.e("color-1", "color - 1:${value}")
                    mFlow.tryEmit(value)
                    temp = value
                }
            }
            animator.start()
        }
    }
}