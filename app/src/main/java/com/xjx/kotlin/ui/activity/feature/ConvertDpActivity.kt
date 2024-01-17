package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityConvertDpBinding

class ConvertDpActivity : BaseBindingTitleActivity<ActivityConvertDpBinding>() {

    override fun getTitleContent(): String {
        return "测试转换dp"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityConvertDpBinding {
        return ActivityConvertDpBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}