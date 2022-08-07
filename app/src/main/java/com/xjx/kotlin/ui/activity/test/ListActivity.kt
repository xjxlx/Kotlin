package com.xjx.kotlin.ui.activity.test

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityListBinding

class ListActivity : AppBaseBindingTitleActivity<ActivityListBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityListBinding {
        return ActivityListBinding.inflate(inflater, container, true)
    }

    override fun setTitleContent(): String {
        return "集合"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initData(savedInstanceState: Bundle?) {

        // 不可变list集合,不能添加和删除元素
        val list = listOf<String>("1", "2")
        LogUtil.e("list:$list")

        // 可变list集合
        val mutableListOf = mutableListOf<String>()
        mutableListOf.add("1")
        mutableListOf.add("2")
        mutableListOf.remove("13")
        LogUtil.e("mutableListOf:$mutableListOf")

        // 普通遍历
        for (item in mutableListOf) {
            LogUtil.e("item:$item")
        }

        // 带角标遍历
        for ((index, item) in mutableListOf.withIndex()) {
            LogUtil.e("index:$index item:$item")
        }

        // foreach 遍历
        mutableListOf.forEachIndexed { index, item ->
            LogUtil.e("forEachIndexed:index:$index item:$item")
        }

        // 不可变set集合
        val of = setOf<String>("张三", "李四")
        // 可变set集合
        val mutableSetOf = mutableSetOf<String>()
        mutableListOf.add("1")
        mutableListOf.removeAt(0)

        // 不可变map集合
        val mapOf = mapOf<String, Int>()
        // 可变map集合
        val mutableMapOf = mutableMapOf<String, Int>()
        mutableMapOf["key1"] = 1
        mutableMapOf["key2"] = 2
        mutableMapOf.remove("key")

        // 普通遍历
        for (item in mutableMapOf) {
            LogUtil.e("mutableMapOf --> item:$item")
            // 打印结果：mutableMapOf --> item:key1=1
            // 打印结果：mutableMapOf --> item:key2=2

            val key = item.key
            val value = item.value
            LogUtil.e("mutableMapOf --> item --> key:$key")
            // mutableMapOf --> item --> key:key1
            LogUtil.e("mutableMapOf --> item --> value:$value")
            // mutableMapOf --> item --> value:1
        }

        val pair1 = Pair("key1", 1)
        val pair2 = Pair("key2", 2)
        val mutableMapOf1 = mutableMapOf(pair1,pair2)

        // Api >= 24 才可以使用
        mutableMapOf1.forEach { (key, value) ->
            LogUtil.e("key:$key value:$value")
        }

    }

}