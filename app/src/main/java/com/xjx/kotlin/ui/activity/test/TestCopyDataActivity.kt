package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityTestCopyDataBinding

class TestCopyDataActivity : BaseBindingTitleActivity<ActivityTestCopyDataBinding>() {

	private val mList = mutableListOf(Test(test = TestIn(a = 1, b = 1)), Test(test = TestIn(a = 2, b = 2)))
	override fun getTitleContent(): String {
		return "测试深拷贝和浅拷贝"
	}

	override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTestCopyDataBinding {
		return ActivityTestCopyDataBinding.inflate(inflater, container, attachToRoot)
	}

	override fun initData(savedInstanceState: Bundle?) {
		val test = Test(TestIn(4, 5))
		LogUtil.e("list:${test}")

		val toList1 = mList.toList()

		val toList = mList.toMutableList()
		toList.removeAt(0)
		LogUtil.e("list -3 -2:${toList}")
		LogUtil.e("list -3:${mList}")

		toList.addAll(mList)
		LogUtil.e("list -3-3:${toList}")
	}

	data class Test(var test: TestIn) : Cloneable {
		public override fun clone(): Test {
			return Test(test)
		}

		override fun toString(): String {
			return "Test(  test=$test)"
		}
	}

	//	data class TestIn(var a: Int, var b: Int) : Cloneable {
//		public override fun clone() = TestIn(a, b)
//	}
	data class TestIn(var a: Int, var b: Int) {

	}
}
