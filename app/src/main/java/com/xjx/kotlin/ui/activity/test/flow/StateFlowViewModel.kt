package com.xjx.kotlin.ui.activity.test.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.helper.utils.LogUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class StateFlowViewModel : ViewModel() {

    // An initial default value must be passed
    private val _stateFlow = MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()

    val stateFlowString = MutableStateFlow("")

    fun repeat() {
        viewModelScope.launch {
            repeat(100) {
                _stateFlow.value += 1
                LogUtil.e("value: " + _stateFlow.value)
                delay(1000)
            }
        }
    }

    fun delaySend() {
        viewModelScope.launch {
            delay(3000)
            _stateFlow.value = 1000
        }
    }

    val flow: Flow<Int> = flow {
        var count = 0
        repeat(100) {
            count++
            emit(count)
            delay(1000)
        }
    }

    fun login() {
        stateFlowString.value = "登录成功"
    }
}