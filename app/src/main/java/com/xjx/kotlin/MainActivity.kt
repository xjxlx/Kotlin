package com.xjx.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.helper.base.BaseBindingActivity
import com.xjx.kotlin.databinding.ActivityMainBinding
import com.xjx.kotlin.ui.activity.test.TestArrayActivity

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    override fun initListener() {
        super.initListener()
        setonClickListener(R.id.tv_item_array)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.tv_item_array -> {
                startActivity(TestArrayActivity::class.java)
            }
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }
}