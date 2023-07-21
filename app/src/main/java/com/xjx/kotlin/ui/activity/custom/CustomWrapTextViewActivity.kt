package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCustomWrapTextViewBinding

class CustomWrapTextViewActivity : AppBaseBindingTitleActivity<ActivityCustomWrapTextViewBinding>() {

    override fun setTitleContent(): String {
        return "自动换行View"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCustomWrapTextViewBinding {
        return ActivityCustomWrapTextViewBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}