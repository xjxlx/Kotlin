package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityChartViewBinding

class ChartViewActivity : BaseBindingTitleActivity<ActivityChartViewBinding>() {

    override fun getTitleContent(): String {
        return "图表view"
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ): ActivityChartViewBinding {
        return ActivityChartViewBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mBinding.btnChart.setOnClickListener {
            val bottomArray = floatArrayOf(0.6f, 0.3f, 0.2f, 0.1f, 0.4f)
            val topArray = floatArrayOf(1f, 0.3f, 0.5f, 0.7f, 0.6f)
            val scoreArray = intArrayOf(20, 30, 50, 20, 83)
            mBinding.cvChart.setChartArray(bottomArray, topArray, scoreArray)
        }
    }
}
