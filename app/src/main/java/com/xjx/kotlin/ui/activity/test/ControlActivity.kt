package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.android.helper.utils.FileUtil
import com.xjx.kotlin.databinding.ActivityControlBinding
import java.io.File

class ControlActivity : BaseBindingTitleActivity<ActivityControlBinding>() {

    override fun getTitleContent(): String {
        return "操作符"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityControlBinding {
        return ActivityControlBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        // when 运算符
        val flag = null
        when (flag) {
            null -> { // 新特性
            }

            1 -> {}

            2 -> {}

            else -> { // default
            }
        }

        val a = 3
        val b = 4
        if (a == 3) {
        }

        if (a.equals(b)) {
        }

        ArrayList<String>()
        arrayListOf<String>()
        arrayOf(3)

        mapOf<String, String>()
        hashMapOf<String, String>()

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
        person.let { it.age = 3 }
        LogUtil.e("person -> let:$person")

        person.run { name = "zs" }
        LogUtil.e("person -> run:$person")

        person.also { it.age }

        person.apply {
            age = 20
            name = "ss"
        }.test()

        val numbers = mutableListOf("one", "two", "three")
        numbers.also { LogUtil.e("在列表添加新元素: $it") }.add("four")

        val appFilesPath = FileUtil.getInstance().getSdTypePublicPath(Environment.DIRECTORY_DOWNLOADS)

        LogUtil.e("appFilesPath:$appFilesPath")

        // 过滤
        val list2 = arrayListOf<String>()
        for (item in 0 until 10) {
            list2.add("" + item)
        }

        // 过滤条件后返回一个新的数组
        list2.filter {
            LogUtil.e("ffff:$it")
            it.toInt() > 5
        }.forEach { LogUtil.e("filter ---> $it") }

        list2.map {
            return@map it.toInt() * 2
        }.forEach { LogUtil.e("m:$it") }

        val path = "D:\\works\\StudioWorks\\Kotlin\\app\\build.gradle"
        File(path).readText().toCharArray().filterNot { // 不包含的过滤
            it.isWhitespace() // 空格
        }.forEach { LogUtil.e("sss:" + it) }
    }

    inner class Person {
        var name: String = ""
        var age: Int = 0
        override fun toString(): String {
            return "Person(name='$name', age=$age)"
        }

        public fun test() {}
    }
}
