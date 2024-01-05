package com.xjx.kotlin.ui.activity.test.flow

// import androidx.lifecycle.repeatOnLifecycle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityFlowBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class FlowActivity : BaseBindingTitleActivity<ActivityFlowBinding>() {

    private val mStateFlow: StateFlowViewModel by lazy {
        ViewModelProvider(this)[StateFlowViewModel::class.java]
    }
    private val mSharedFlow: ShareFlowViewModel by lazy {
        ViewModelProvider(this)[ShareFlowViewModel::class.java]
    }
    private val mSharedFlow2 = MutableSharedFlow<String>(replay = 0)

    //    private val mSharedFlow2 = MutableStateFlow("1")
    private val mFlowViewModel: FlowViewModel by viewModels()
    private var mNumber = 0
    override fun getTitleContent(): String {
        return "Flow 的使用"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityFlowBinding {
        return ActivityFlowBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        //        lifecycleScope.launch {
        //            // stateFlow 粘性的flow
        //            repeatOnLifecycle(Lifecycle.State.STARTED) {
        //                mStateFlow.stateFlow.collect {
        //                    delay(2000)
        //                    mBinding.tvContent.text = it.toString()
        //                    LogUtil.e("收到  stateFlow : $it")
        //                }
        //            }
        //        }

        // string stateFlow
        //        lifecycleScope.launch {
        //            mStateFlow.stringFlow.collect() {
        //                ToastUtil.show(it)
        //                LogUtil.e("粘性： $it")
        //            }
        //        }
        mBinding.btnClickStateFlow.setOnClickListener {
            //            lifecycleScope.launch {
            ////                mStateFlow.repeat()
            ////                mStateFlow.login()
            //
            //                LogUtil.e("viewModel", "1-> ${mFlowViewModel.bean}")
            //
            //                mFlowViewModel.bean.name = "李四"
            //                mFlowViewModel.bean.age = mFlowViewModel.count++
            //
            //                LogUtil.e("viewModel", "2-> ${mFlowViewModel.bean}")
            //                mFlowViewModel.mStateFlow.emit(mFlowViewModel.bean)
            //
            //            }

            lifecycleScope.launchWhenStarted {
                LogUtil.e("XJX", Thread.currentThread().name)

                repeat(100) {
                    mSharedFlow2.emit("" + mNumber)
                    mNumber++
                    delay(200)
                }
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

        // test sharedFlow2
        lifecycleScope.launch { mSharedFlow2.collect { LogUtil.e("jsq_1:$it") } }
        lifecycleScope.launch { mSharedFlow2.collect { LogUtil.e("jsq_2:$it") } }

        lifecycleScope.launch {
            mSharedFlow2.emit("a")
            mSharedFlow2.emit("b")
            mSharedFlow2.emit("c")
            mSharedFlow2.emit("c")
        }

        lifecycleScope.launch {
            mSharedFlow2.collect {
                //                delay(3000)
                LogUtil.e("jsq_3:$it")
            }
        }

        mBinding.btnClickSharedFlow.setOnClickListener {
            lifecycleScope.launch { mSharedFlow2.collect { LogUtil.e("jsq_4:$it") } }
        }

        // 转换为shared
        mSharedFlow2.shareIn(lifecycleScope, SharingStarted.WhileSubscribed(500), 1)

        lifecycleScope.launch {
            //            repeatOnLifecycle(Lifecycle.State.STARTED) {
            //                mFlowViewModel.mStateFlow.collect() {
            //                    LogUtil.e("viewModel", "result:---> $it")
            //                    ToastUtil.show("" + it)
            //                }
            //            }
        }
    }
}
