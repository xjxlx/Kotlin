package com.xjx.kotlin.ui.activity.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityDescriptionViewBinding

class DescriptionViewActivity : AppBaseBindingTitleActivity<ActivityDescriptionViewBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun setTitleContent(): String {
        return "文字描述的View"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityDescriptionViewBinding {
        return ActivityDescriptionViewBinding.inflate(inflater, container, true)
    }
}