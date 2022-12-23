package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityFsBinding

/**
 * 反射
 */
class FsActivity : AppBaseBindingTitleActivity<ActivityFsBinding>() {

    override fun setTitleContent(): String {
        return "反射"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityFsBinding {
        return ActivityFsBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

}