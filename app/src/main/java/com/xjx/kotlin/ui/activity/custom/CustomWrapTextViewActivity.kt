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
        mBinding.btnStart.setOnClickListener {
            mBinding.twvContent.setExplain(20,"加油","下次努力哦！","呼吸时长足够，注意保持均匀的呼吸次数和平缓的心率，让情绪更稳定些效果更好哦！")
        }
    }
}