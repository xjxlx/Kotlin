package com.xjx.kotlin.ui.activity.test.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.helper.utils.LogUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ShareFlowViewModel : ViewModel() {

    private val _shareFlow = MutableSharedFlow<Int>(replay = 1) // 可读可写
    val sharedFlow: SharedFlow<Int> = _shareFlow.asSharedFlow() // 可读，不可写
    val stringSharedFlow = MutableSharedFlow<String>(replay = 10)

    fun repeat() {
        viewModelScope.launch {
            var count = 0
            repeat(100) {
                count++
                _shareFlow.emit(count)

                LogUtil.e("shared: --->count : $count")
                delay(1000)
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            stringSharedFlow.emit(" login success ")
        }
    }
}