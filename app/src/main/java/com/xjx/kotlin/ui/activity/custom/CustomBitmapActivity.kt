package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCustomBitmapBinding

class CustomBitmapActivity : BaseBindingTitleActivity<ActivityCustomBitmapBinding>() {

    override fun getTitleContent(): String {
        return "测试Bitmap"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityCustomBitmapBinding {
        return ActivityCustomBitmapBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}