package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityChartViewBinding

class ChartViewActivity : AppBaseBindingTitleActivity<ActivityChartViewBinding>() {

    override fun setTitleContent(): String {
        return "图表view"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityChartViewBinding {
        return ActivityChartViewBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mBinding.btnChart.setOnClickListener {
            val floatArray = floatArrayOf(0.8f, 1f, 0.2f, 0.1f, 0.4f)
            mBinding.cvChart.setChartArray(floatArray)
        }
    }
}