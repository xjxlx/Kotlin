package com.xjx.kotlin.ui.activity.test.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityTestMutableFlowBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TestMutableFlowActivity : BaseBindingTitleActivity<ActivityTestMutableFlowBinding>() {
    private var count = 1
    private val mSharedFlow2: MutableSharedFlow<Int> = MutableStateFlow(0)
    private val TAG = "FLOW"
    private val mLiveData: MutableLiveData<Int> = MutableLiveData<Int>()
    private var mCount = 0
    private val mStateFlow: MutableStateFlow<Data> = MutableStateFlow(Data())
    private var mEntity: Data = Data()

    override fun getTitleContent(): String = "测试mutableFlow"

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): ActivityTestMutableFlowBinding = ActivityTestMutableFlowBinding.inflate(inflater, container, true)

    override fun initData(savedInstanceState: Bundle?) {
        mLiveData.observe(
            this,
            object : Observer<Int> {
                override fun onChanged(value: Int) {
                    LogUtil.e(TAG, "value1：$value")
                }
            }
        )
        mLiveData.observe(
            this,
            object : Observer<Int> {
                override fun onChanged(value: Int) {
                    LogUtil.e(TAG, "value2：$value")
                }
            }
        )
        mLiveData.observe(
            this,
            object : Observer<Int> {
                override fun onChanged(value: Int) {
                    LogUtil.e(TAG, "value3：$value")
                }
            }
        )
        lifecycleScope.launch {
            mLiveData.asFlow().collect {
                LogUtil.e(TAG, "value-flow-1：$it")
            }
        }
        lifecycleScope.launch {
            mLiveData.asFlow().collect {
                LogUtil.e(TAG, "value-flow-2：$it")
            }
        }
        lifecycleScope.launch {
            mLiveData.asFlow().collect {
                LogUtil.e(TAG, "value-flow-3：$it")
            }
        }
        lifecycleScope.launch {
            mStateFlow.collect {
                LogUtil.e("state-flow-:$it")
            }
        }

        mBinding.btnSendLiveData.setOnClickListener {
            mLiveData.value = mCount++
        }

        mBinding.btnSendStateData.setOnClickListener {
            mEntity.finishFlag += 1
            mStateFlow.value = mEntity
        }
    }

    class DataManager {
        val mSharedFlow: MutableSharedFlow<Int> = MutableStateFlow(0)

        // 发送数据
        fun sendData(data: Int) {
            mSharedFlow.tryEmit(data)
        }
    }

    class Data : Cloneable {
        var finishFlag = 1
        var dataType: DataType = DataType()

        override fun toString(): String = "Data(finishFlag=$finishFlag, dataType=$dataType)"

        public override fun clone(): Data = super.clone() as Data
    }

    class DataType {
        var type: Int = 0

        override fun toString(): String = "DataType(type=$type)"
    }
}
