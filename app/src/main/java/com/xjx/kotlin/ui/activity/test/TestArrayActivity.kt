package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityTestArrayBinding

class TestArrayActivity : BaseBindingTitleActivity<ActivityTestArrayBinding>() {

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
        intArrayOf.forEach { item -> LogUtil.e("foreach:$item") }

        // forEach遍历，带角标
        intArrayOf.forEachIndexed { index, item -> LogUtil.e("foreach: index:$index item:$item") }

        // 打印数组中的内容
        val contentToString = intArrayOf.contentToString()
        LogUtil.e(contentToString)

        if (1 in intArrayOf) {
            // 是否包含1
        }

        if (5 !in intArrayOf) {
            // 是否包含5
        }

        LogUtil.e("集合中是否包含 1：${1 in intArrayOf}")
        LogUtil.e("集合中是否包含 5：${5 in intArrayOf}")
        LogUtil.e("集合中是否不包含 5：${5 !in intArrayOf}")

        // 创建可变数组
        val arrayOf = arrayOf("1")

        // 创建指定大小的不可见数组
        val array = Array(4) { it + 1 }
        LogUtil.e(array.contentToString())

        // 区间
        val intRange = 1..10
        LogUtil.e("区间内容：${intRange.joinToString()}")

        // 闭区间 .. (完全打印所有内容）0..10
        for (item in 0..10) {
            LogUtil.e("item:$item")
        }

        // 半开区间 until（包左不包右）0..9
        for (item in 0 until 10) {
            LogUtil.e("item2:$item")
        }

        // 半开区间 indices == until
        for (index in intArrayOf.indices) {
            LogUtil.e("indices:${intArrayOf[index]}")
        }
    }

    override fun getTitleContent(): String {
        return "数组"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTestArrayBinding {
        return ActivityTestArrayBinding.inflate(inflater)
    }
}
