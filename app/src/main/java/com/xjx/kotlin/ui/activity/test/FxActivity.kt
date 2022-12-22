package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityFxBinding

class FxActivity : AppBaseBindingTitleActivity<ActivityFxBinding>() {

    override fun setTitleContent(): String {
        return "泛型"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityFxBinding {
        return ActivityFxBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}