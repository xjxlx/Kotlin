package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityAdbBinding

class AdbActivity : BaseBindingTitleActivity<ActivityAdbBinding>() {

    override fun getTitleContent(): String {
        return "使用ADB功能"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityAdbBinding {
        return ActivityAdbBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}