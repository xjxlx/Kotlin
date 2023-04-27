package com.xjx.kotlin.ui.activity.test.flow

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.LogUtil
import com.android.apphelper2.utils.permission.PermissionCallBackListener
import com.android.apphelper2.utils.permission.PermissionRationaleCallBackListener
import com.android.apphelper2.utils.permission.PermissionUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityFlowCallBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
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

//            lifecycleScope.launch {
//                repeat(100) {
//                    LogUtil.e("sample ---> send ---> ", " send ---> $it")
//                    mSharedFlow.emit(it)
//                    delay(1000)
//                }
//            }

            mPermission.shouldShow(Manifest.permission.READ_EXTERNAL_STORAGE, object : PermissionRationaleCallBackListener {
                override fun onCallBack(permission: String, rationale: Boolean) {
                    LogUtil.e("--->permission  : rationale: $rationale")

                    mPermission.setCallBackListener(object : PermissionCallBackListener {
                        override fun onCallBack(permission: String, isGranted: Boolean) {
                            LogUtil.e("--->permission  : isGranted: $isGranted")

                        }
                    })
                        .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            })
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
//        val flow = callbackFlow {
//            requestApi {
//                // 发送成功的数据
//                trySend("2")
//
//                // 发送错误的数据
//                close(NullPointerException("error"))
//            }
//
//            // 关闭的回调
//            awaitClose {
//                // 关闭的回调
//                LogUtil.e("awaitClose ---->")
//            }
//        }

        lifecycleScope.launch {
            mSharedFlow.sample(3000)
                .debounce(1000)
                .onEach {

                }
                .collect() {
                    LogUtil.e("sample ---> result ---> ", "result ---> $it")
                }
        }

        val flow = flow<Int> {
            emit(1)
        }

    }

}