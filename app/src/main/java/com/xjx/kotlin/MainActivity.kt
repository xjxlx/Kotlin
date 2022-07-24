package com.xjx.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.helper.utils.LogUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intArrayOf = intArrayOf(1, 2, 3)

        // 遍历集合，不带角标
        for (item in intArrayOf) {
            LogUtil.e("-$item")
        }
    }
}