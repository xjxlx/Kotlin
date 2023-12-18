package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityTestCopyDataBinding

class TestCopyDataActivity : BaseBindingTitleActivity<ActivityTestCopyDataBinding>() {

	override fun getTitleContent(): String {
		return "测试深拷贝和浅拷贝"
	}

	override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTestCopyDataBinding {
		return ActivityTestCopyDataBinding.inflate(inflater, container, attachToRoot)
	}

	override fun initData(savedInstanceState: Bundle?) {
		val list = mutableListOf("1", Test(3, 4))
		LogUtil.e("list:${list}")

		val toMutableList = list.toMutableList()
		val get = toMutableList[1] as Test
		get.a = 33333
		LogUtil.e("list -1:${list}")
		LogUtil.e("list -1 list:${toMutableList}")

		val toList = list.toList()
		(toList[1] as Test).clone().a = 666
		LogUtil.e("list - 2:${list}")
		LogUtil.e("list - 2:toList:${toList}")
	}

	data class Test(var a: Int, var b: Int) : Cloneable {
		public override fun clone(): Test {
			return super.clone() as Test
		}

		override fun toString(): String {
			return "Test(a=$a, b=$b)"
		}
	}
}