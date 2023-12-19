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
		val test = Test(3, 45)
		LogUtil.e("list:${test}")

		val clone = test.clone()
		clone.a = 666
		val copy = test.copy()
		copy.a = 777
		LogUtil.e("list - clone:${clone}")
		LogUtil.e("list - copy:${copy}")
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