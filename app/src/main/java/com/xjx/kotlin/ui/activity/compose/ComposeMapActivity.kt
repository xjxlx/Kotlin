package com.xjx.kotlin.ui.activity.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityComposeMapBinding

class ComposeMapActivity : BaseBindingTitleActivity<ActivityComposeMapBinding>() {

    override fun getTitleContent(): String {
        return "Compose集合"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityComposeMapBinding {
        return ActivityComposeMapBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}