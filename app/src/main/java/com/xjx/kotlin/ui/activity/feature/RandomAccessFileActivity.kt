package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.apphelper2.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityRandomAccessFileBinding

class RandomAccessFileActivity : BaseBindingTitleActivity<ActivityRandomAccessFileBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityRandomAccessFileBinding {
        return ActivityRandomAccessFileBinding.inflate(inflater, container, true)
    }

    override fun getTitleContent(): String {
        return "测试随机读写流"
    }
}