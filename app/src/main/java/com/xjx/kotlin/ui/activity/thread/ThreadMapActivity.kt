package com.xjx.kotlin.ui.activity.thread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityThreadMapBinding

class ThreadMapActivity : BaseBindingTitleActivity<ActivityThreadMapBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.tvItemThread.setOnClickListener {
            startActivity(TestConcurrenceThreadActivity::class.java)
        }
        mBinding.tvItemThreadJava.setOnClickListener {
            startActivity(ConcurrenceThread2Activity::class.java)
        }
    }

    override fun getTitleContent(): String {
        return "ThreadMap"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityThreadMapBinding {
        return ActivityThreadMapBinding.inflate(inflater, container, true)
    }
}
