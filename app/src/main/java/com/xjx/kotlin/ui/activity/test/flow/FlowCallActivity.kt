package com.xjx.kotlin.ui.activity.test.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.LogUtil
import com.android.apphelper2.utils.permission.PermissionUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityFlowCallBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch

class FlowCallActivity : AppBaseBindingTitleActivity<ActivityFlowCallBinding>() {

    private val mSharedFlow = MutableSharedFlow<Int>()
    private val mPermission = PermissionUtil.PermissionActivity(this)

    override fun setTitleContent(): String {
        return "Flow Call"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityFlowCallBinding {
        return ActivityFlowCallBinding.inflate(inflater, container, true)
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnStart.setOnClickListener {

            lifecycleScope.launch {

                repeat(100) {
                    mSharedFlow.emit(it)
                    delay(100)
                }
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {

        lifecycleScope.launch {
            mSharedFlow.sample(1000)
                .collect() {
                    LogUtil.e("sample ---> result ---> ", "result ---> $it")
                }
        }
    }
}