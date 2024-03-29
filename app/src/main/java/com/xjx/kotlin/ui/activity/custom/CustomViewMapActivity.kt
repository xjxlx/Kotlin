package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCustomViewMapBinding

class CustomViewMapActivity : BaseBindingTitleActivity<ActivityCustomViewMapBinding>() {
    override fun initData(savedInstanceState: Bundle?) {
        mBinding.tvItemDescription.setOnClickListener {
            startActivity(DescriptionViewActivity::class.java)
        }

        mBinding.tvItemRecycleview.setOnClickListener { startActivity(RecycleViewActivity::class.java) }
        mBinding.tvItemChart.setOnClickListener { startActivity(ChartViewActivity::class.java) }
        mBinding.tvItemScoreProgress.setOnClickListener {
            startActivity(CustomProgressActivity::class.java)
        }
        mBinding.tvItemTextWrap.setOnClickListener {
            startActivity(CustomWrapTextViewActivity::class.java)
        }
        mBinding.tvItemScoreResult.setOnClickListener {
            startActivity(ScoreSummarizeActivity::class.java)
        }
        mBinding.tvItemTablayout.setOnClickListener {
            startActivity(CustomTabLayoutActivity::class.java)
        }
        mBinding.tvItemTestBitmap.setOnClickListener {
            startActivity(CustomBitmapActivity::class.java)
        }
        mBinding.tvItemTestLine.setOnClickListener {
            startActivity(CustomLineActivity::class.java)
        }
        mBinding.tvItemBanner.setOnClickListener {
            startActivity(CustomBannerActivity::class.java)
        }
    }

    override fun getTitleContent(): String {
        return "自定义View集合"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?,
        attachToRoot: Boolean): ActivityCustomViewMapBinding {
        return ActivityCustomViewMapBinding.inflate(inflater, container, true)
    }
}
