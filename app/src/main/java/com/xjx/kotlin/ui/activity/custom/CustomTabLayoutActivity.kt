package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.apphelper2.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCustomTabLayoutBinding

class CustomTabLayoutActivity : BaseBindingTitleActivity<ActivityCustomTabLayoutBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityCustomTabLayoutBinding {
        return ActivityCustomTabLayoutBinding.inflate(inflater, container, true)
    }

    override fun getTitleContent(): String {
        return "TabLayout"
    }
}