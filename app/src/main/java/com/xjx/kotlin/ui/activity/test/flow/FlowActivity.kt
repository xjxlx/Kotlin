package com.xjx.kotlin.ui.activity.test.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityFlowBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FlowActivity : AppBaseBindingTitleActivity<ActivityFlowBinding>() {

    private val mStateFlow: StateFlowViewModel by lazy {
        ViewModelProvider(this)[StateFlowViewModel::class.java]
    }
    private val mSharedFlow: ShareFlowViewModel by lazy {
        ViewModelProvider(this)[ShareFlowViewModel::class.java]
    }

    override fun setTitleContent(): String {
        return "Flow 的使用"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityFlowBinding {
        return ActivityFlowBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            // stateFlow 粘性的flow
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mStateFlow.stateFlow.collect {
                    delay(2000)
                    mBinding.tvContent.text = it.toString()
                    LogUtil.e("收到  stateFlow : $it")
                }
            }
        }
        // string stateFlow
//        lifecycleScope.launch {
//            mStateFlow.stringFlow.collect() {
//                ToastUtil.show(it)
//                LogUtil.e("粘性： $it")
//            }
//        }
        mBinding.btnClickStateFlow.setOnClickListener {
            lifecycleScope.launch {
                mStateFlow.repeat()
                mStateFlow.login()
            }
        }
        // test flow
//        lifecycleScope.launch {
//            mStateFlow.flow.collect() {
//                mBinding.tvContent.text = it.toString()
//            }
//        }
        // test flowConvertStateFlow
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                mStateFlow.flowConvertStateflow.collect {
//                    mBinding.tvContent.text = it.toString()
//                }
//            }
//        }
        // shared flow
//        mBinding.btnClickSharedFlow.setOnClickListener {
//            mSharedFlow.login()
//            mSharedFlow.repeat()
//        }

//        lifecycleScope.launch {
//            delay(2000)
//            mSharedFlow.sharedFlow.collect {
//                mBinding.tvContent.text = it.toString()
//            }
//        }

//        lifecycleScope.launch {
//            mSharedFlow.stringSharedFlow.collect {
//                ToastUtil.show(it)
//            }
//        }
    }
}