package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCustomProgressBinding

class CustomProgressActivity : AppBaseBindingTitleActivity<ActivityCustomProgressBinding>() {

    override fun setTitleContent(): String {
        return "综合评分"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCustomProgressBinding {
        return ActivityCustomProgressBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

        mBinding.btnStart.setOnClickListener {

            var value = mBinding.etNumber.text.toString()
            if (TextUtils.isEmpty(value)) {
                value = "0"
            }
            val toFloat = value.toFloat()
            mBinding.spvScore.setScore(toFloat)
        }
    }
}