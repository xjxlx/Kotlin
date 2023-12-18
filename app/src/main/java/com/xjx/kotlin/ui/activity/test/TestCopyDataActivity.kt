package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityTestCopyDataBinding

class TestCopyDataActivity : BaseBindingTitleActivity<ActivityTestCopyDataBinding>() {

	override fun getTitleContent(): String {
		return "测试深拷贝和浅拷贝"
	}

	override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTestCopyDataBinding {
		return ActivityTestCopyDataBinding.inflate(inflater, container, attachToRoot)
	}

	override fun initData(savedInstanceState: Bundle?) {
	}
}