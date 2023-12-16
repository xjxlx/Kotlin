package com.xjx.kotlin.ui.activity.test.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityTestMutableFlowBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TestMutableFlowActivity : BaseBindingTitleActivity<ActivityTestMutableFlowBinding>() {

	private val mStateFlow: MutableStateFlow<Int> = MutableStateFlow(0)

	override fun getTitleContent(): String {
		return "测试mutableFlow"
	}

	override fun getBinding(
		inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean
	): ActivityTestMutableFlowBinding {
		return ActivityTestMutableFlowBinding.inflate(inflater, container, true)
	}

	override fun initData(savedInstanceState: Bundle?) {
		lifecycleScope.launch {
			mStateFlow.collect {
				LogUtil.e("collect:${it}")
			}
		}
		var a = 1
		mBinding.btnSend.setOnClickListener {
			lifecycleScope.launch {
				mStateFlow.emit(a)
			}
		}
	}
}