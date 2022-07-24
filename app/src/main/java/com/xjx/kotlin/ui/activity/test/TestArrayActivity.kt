package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.BaseBindingActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityTestArrayBinding

class TestArrayActivity : BaseBindingActivity<ActivityTestArrayBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        val intArrayOf = intArrayOf(1, 2, 3)

        // 遍历集合，不带角标
        for (item in intArrayOf) {
            LogUtil.e("-$item")
        }

        // 遍历集合，带角标
        for ((index, item) in intArrayOf.withIndex()) {
            LogUtil.e("index: $index, item: $item") // 输出: index: 0, item: 1....index: 13, item: 2
        }

        // forEach遍历，不到角标
        intArrayOf.forEach { item ->
            LogUtil.e("foreach:$item")
        }

        // forEach遍历，带角标
        intArrayOf.forEachIndexed { index, item ->
            LogUtil.e("foreach: index:$index item:$item")
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityTestArrayBinding {
        return ActivityTestArrayBinding.inflate(inflater)
    }
}