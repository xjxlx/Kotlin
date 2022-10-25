package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
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
        val arrayOf = arrayOf(3)

        val mapOf = mapOf<String, String>()
        val hashMapOf = hashMapOf<String, String>()

        val list1 = arrayListOf<String>()
        for (item in 0..10) {
            list1.add("" + item)
        }
        LogUtil.e("list:" + list1)

        list1.forEach abc@{ fe ->
            if (fe == "2") {
                return@abc
            }
        }

//        run loop@{
//            listOf(1, 2, 3, 4, 5).forEach {
//                if (it == 3) return@loop // 从传入 run 的 lambda 表达式非局部返回
//                LogUtil.e("list: it --->$it")
//            }
//        }

        listOf(1, 2, 3, 4, 5).forEach loop@{
            if (it < 3) return@loop // 从传入 run 的 lambda 表达式非局部返回
            LogUtil.e("list: it --->$it")
        }

        print(" done with nested loop")

        var person = Person()
        person.let {
            it.age = 3
        }
        LogUtil.e("person -> let:$person")

        person.run {
            name = "zs"
        }
        LogUtil.e("person -> run:$person")

        val also = person.also {
            it.age
        }

        val apply = person.apply {
            age
        }

    }

    inner class Person {
        var name: String = ""
        var age: Int = 0
        override fun toString(): String {
            return "Person(name='$name', age=$age)"
        }

    }

}