package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.R
import com.xjx.kotlin.databinding.ActivityFeatureMapBinding

class FeatureMapActivity : AppBaseBindingTitleActivity<ActivityFeatureMapBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityFeatureMapBinding {
        return ActivityFeatureMapBinding.inflate(inflater, container, true)
    }

    override fun setTitleContent(): String {
        return "功能测试"
    }

    override fun initData(savedInstanceState: Bundle?) {
        setonClickListener(R.id.tv_item_recording_video)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.tv_item_recording_video -> {
                startActivity(RecordActivity::class.java)
            }
        }
    }
}