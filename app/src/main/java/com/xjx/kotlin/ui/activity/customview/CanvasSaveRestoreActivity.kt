package com.xjx.kotlin.ui.activity.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCanvasSaveRestoreBinding

class CanvasSaveRestoreActivity : AppBaseBindingTitleActivity<ActivityCanvasSaveRestoreBinding>() {

    override fun setTitleContent(): String {
        return "Canvas - Save - Restore"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCanvasSaveRestoreBinding {
        return ActivityCanvasSaveRestoreBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}