package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityTextToSpeechBinding

class TextToSpeechActivity : BaseBindingTitleActivity<ActivityTextToSpeechBinding>() {

    override fun getTitleContent(): String {
        return "文字转换语音"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTextToSpeechBinding {
        return ActivityTextToSpeechBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}