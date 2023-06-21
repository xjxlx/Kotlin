package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityRecordBinding

class RecordActivity : AppBaseBindingTitleActivity<ActivityRecordBinding>() {

    override fun setTitleContent(): String {
        return "录制视频"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityRecordBinding {
        return ActivityRecordBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}