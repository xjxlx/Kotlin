package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCurrentIpBinding

class CurrentIpActivity : AppBaseBindingTitleActivity<ActivityCurrentIpBinding>() {

    override fun setTitleContent(): String {
        return "当前的ip"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCurrentIpBinding {
        return ActivityCurrentIpBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}