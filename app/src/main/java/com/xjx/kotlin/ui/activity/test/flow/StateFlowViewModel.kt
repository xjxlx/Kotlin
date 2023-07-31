package com.xjx.kotlin.ui.activity.test.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.common.utils.LogUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StateFlowViewModel : ViewModel() {

    private val _stateFlow = MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()
    val stringFlow = MutableStateFlow("")

    fun repeat() {
        viewModelScope.launch {
            var count = 0
            repeat(100) {
                _stateFlow.emit(count)
                LogUtil.e("count: $count")
                delay(1000)
            }
        }
    }

    suspend fun login() {
        stringFlow.emit("login success")
    }

    val flow: Flow<Int> = flow {
        var count = 0
        repeat(100) {
            count++
            emit(count)
            delay(1000)
        }
    }

    val flowConvertStateflow = flow.stateIn(
        viewModelScope, // 指定的协程域
        SharingStarted.WhileSubscribed(1000), // 指定的超时时间，这里指的是，在不超过指定时间内，不去停止flow，如果超过了就停止flow
        0 // 默认值
    )
}