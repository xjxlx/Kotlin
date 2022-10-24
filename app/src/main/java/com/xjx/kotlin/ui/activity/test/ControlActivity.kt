package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityControlBinding

class ControlActivity : AppBaseBindingTitleActivity<ActivityControlBinding>() {

    override fun setTitleContent(): String {
        return "操作符"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityControlBinding {
        return ActivityControlBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        // when 运算符
        val flag = null
        when (flag) {
            null -> { // 新特性

            }
            1 -> {

            }
            2 -> {

            }
            else -> { // default

            }
        }

        val a = 3;
        val b = 4;
        if (a == 3) {

        }
        if (a.equals(b)) {

        }

        val arrayList = ArrayList<String>()
        val arrayListOf = arrayListOf<String>()
        val arrayList1 = ArrayList<String>()
        val arrayList2 = ArrayList<String>()
        val arrayOf = arrayOf(3)

        val mapOf = mapOf<String, String>()
        val hashMapOf = hashMapOf<String, String>()
        val mapOf1 = mapOf<String, String>()


    }

}