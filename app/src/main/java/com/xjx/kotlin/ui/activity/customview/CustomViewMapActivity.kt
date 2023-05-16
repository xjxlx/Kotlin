package com.xjx.kotlin.ui.activity.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCustomViewMapBinding

class CustomViewMapActivity : AppBaseBindingTitleActivity<ActivityCustomViewMapBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.tvItemDescription.setOnClickListener {
            startActivity(DescriptionViewActivity::class.java)
        }
    }

    override fun setTitleContent(): String {
        return "自定义View集合"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCustomViewMapBinding {
        return ActivityCustomViewMapBinding.inflate(inflater, container, true)
    }
}