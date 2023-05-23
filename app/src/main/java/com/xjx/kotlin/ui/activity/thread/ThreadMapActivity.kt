package com.xjx.kotlin.ui.activity.thread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityThreadMapBinding

class ThreadMapActivity : AppBaseBindingTitleActivity<ActivityThreadMapBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.tvItemThread.setOnClickListener {
            startActivity(TestConcurrenceThreadActivity::class.java)
        }
    }

    override fun setTitleContent(): String {
        return "ThreadMap"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityThreadMapBinding {
        return ActivityThreadMapBinding.inflate(inflater, container, true)
    }
}