package com.xjx.kotlin.ui.activity.animations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityAnimationJsonBinding

class AnimationJsonActivity : BaseBindingTitleActivity<ActivityAnimationJsonBinding>() {

    override fun getTitleContent(): String {
        return "JSON动画"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityAnimationJsonBinding {
        return ActivityAnimationJsonBinding.inflate(inflater, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}