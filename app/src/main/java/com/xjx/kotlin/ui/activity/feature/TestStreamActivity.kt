package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityTestStreamBinding
import java.util.stream.Stream

class TestStreamActivity : BaseBindingTitleActivity<ActivityTestStreamBinding>() {
    override fun getTitleContent(): String = "测试Stream"

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): ActivityTestStreamBinding = ActivityTestStreamBinding.inflate(inflater, container, attachToRoot)

    override fun initData(savedInstanceState: Bundle?) {
        val listOf = mutableListOf("张三", "李四", "王五")

        mBinding.btnEmptyTest.setOnClickListener {
            val empty = Stream.empty<String>()
            LogUtil.e("empty:${empty.count()}")
        }
        mBinding.btnListTest.setOnClickListener {
            val stream = listOf.stream().count()
            LogUtil.e("list:stream:$stream")
            listOf.stream().forEach {
                LogUtil.e("list:$it")
            }
        }
    }
}
