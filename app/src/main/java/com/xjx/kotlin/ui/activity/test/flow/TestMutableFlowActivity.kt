package com.xjx.kotlin.ui.activity.test.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityTestMutableFlowBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TestMutableFlowActivity : BaseBindingTitleActivity<ActivityTestMutableFlowBinding>() {

	private var count = 1
	val mSharedFlow2: MutableSharedFlow<Int> = MutableStateFlow(0)

	override fun getTitleContent(): String {
		return "测试mutableFlow"
	}

	override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTestMutableFlowBinding {
		return ActivityTestMutableFlowBinding.inflate(inflater, container, true)
	}

	override fun initData(savedInstanceState: Bundle?) {
		val dataManager = DataManager()

		lifecycleScope.launch {
			dataManager.mSharedFlow.collect {
				LogUtil.e("flow", "shared - collect:${it}")
			}

			mSharedFlow2.collect() {
				LogUtil.e("flow", "shared - 2 - collect:${it}")
			}
		}

		mBinding.btnSend.setOnClickListener {
			count++
			LogUtil.e("flow", "shared - send:${count}")
			// dataManager.sendData(count)
//			dataManager.mSharedFlow.tryEmit(count)

			mSharedFlow2.tryEmit(count)
		}
	}

	class DataManager {
		val mSharedFlow: MutableSharedFlow<Int> = MutableStateFlow(0)

		// 发送数据
		fun sendData(data: Int) {
			mSharedFlow.tryEmit(data)
		}
	}
}