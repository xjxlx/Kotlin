package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityColorConvertBinding

class ColorConvertActivity : BaseBindingTitleActivity<ActivityColorConvertBinding>() {

    override fun getTitleContent(): String {
        return "测试颜色转换"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityColorConvertBinding {
        return ActivityColorConvertBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}