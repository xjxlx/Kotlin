package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
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
    }
}