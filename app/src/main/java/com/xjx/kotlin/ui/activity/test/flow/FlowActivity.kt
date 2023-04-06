package com.xjx.kotlin.ui.activity.test.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityFlowBinding
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

//        lifecycleScope.launch {
            // stateFlow 粘性的flow
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                mStateFlow.stateFlow.collect {
//                    mBinding.tvContent.text = "" + it
//                }
//            }

            // 纯的flow
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                mStateFlow.flow.collect {
//                    mBinding.tvContent.text = "" + it
//                }
//            }
//        }

        mBinding.btnClick.setOnClickListener {
            // mStateFlow.repeat()

            // 延迟加载
//            mStateFlow.delaySend()

//            lifecycleScope.launch {
//                mStateFlow.stateFlow.collect {
//                    mBinding.tvContent.text = it.toString()
//                }
//            }
        }

        // string flow
//        lifecycleScope.launch {
//            mStateFlow.login()
//            mStateFlow.stateFlowString.collect {
//                ToastUtil.show(it)
//            }
//        }

//        lifecycleScope.launch {
////            mSharedFlow.repeat()
//            mSharedFlow.sharedFlow.collect {
//                mBinding.tvContent.text = it.toString()
//            }
//        }


    }
}