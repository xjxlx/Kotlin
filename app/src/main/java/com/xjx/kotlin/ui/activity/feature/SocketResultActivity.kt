package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivitySocketResultBinding

class SocketResultActivity : AppBaseBindingTitleActivity<ActivitySocketResultBinding>() {

    override fun setTitleContent(): String {
        return "Socket - 接收端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivitySocketResultBinding {
        return ActivitySocketResultBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}