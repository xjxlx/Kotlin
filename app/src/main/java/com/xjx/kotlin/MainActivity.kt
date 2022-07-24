package com.xjx.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.mapcore.util.it
import com.android.helper.utils.LogUtil
import okhttp3.internal.immutableListOf

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
    }

    private fun initData() {
        val intArrayOf = intArrayOf(1, 2, 3)

        // 遍历集合，不带角标
        for (item in intArrayOf) {
            LogUtil.e("-$item")
        }

        // 遍历集合，带角标
        for ((index, item) in intArrayOf.withIndex()) {
            LogUtil.e("index: $index, item: $item") // 输出: index: 0, item: 1....index: 13, item: 2
        }

    }
}