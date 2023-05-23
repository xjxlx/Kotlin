package com.xjx.kotlin.ui.activity.thread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityTestConcurrenceThreadBinding

class TestConcurrenceThreadActivity : AppBaseBindingTitleActivity<ActivityTestConcurrenceThreadBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun setTitleContent(): String {
        return "测试并发的线程"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityTestConcurrenceThreadBinding {
        return ActivityTestConcurrenceThreadBinding.inflate(inflater, container, true)
    }
}