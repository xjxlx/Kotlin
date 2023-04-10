package com.xjx.kotlin.ui.activity.test.block

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityBlockBinding

class BlockActivity : AppBaseBindingTitleActivity<ActivityBlockBinding>() {

    override fun setTitleContent(): String {
        return "Block 语法"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityBlockBinding {
        return ActivityBlockBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        // 无参 无返回
        testBlockNoArguments {
            LogUtil.e("it ----> " + it)
        }
    }

    private fun testBlockNoArguments(block: (arg: String) -> Unit) {
        LogUtil.e("无参 ---> 无返回 ！")
        block("abc")
    }

    fun testBlockNoArguments() {
    }
}