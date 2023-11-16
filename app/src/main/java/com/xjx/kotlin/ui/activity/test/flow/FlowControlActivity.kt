package com.xjx.kotlin.ui.activity.test.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.common.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityFlowControlBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class FlowControlActivity : AppBaseBindingTitleActivity<ActivityFlowControlBinding>() {

    private val mFlow: MutableSharedFlow<Boolean> = MutableSharedFlow()

    override fun setTitleContent(): String {
        return "Flow 操作符"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityFlowControlBinding {
        return ActivityFlowControlBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.btnStart.setOnClickListener {
            lifecycleScope.launch {
                LogUtil.e("debounce", "发送数据！")
                mFlow.emit(true)
            }
        }

        // 1: 超时操作符
        lifecycleScope.launch {
            mFlow.debounce(1000 * 5).collect {
                    LogUtil.e("debounce", "初始化数据！")
                }
        }
    }
}