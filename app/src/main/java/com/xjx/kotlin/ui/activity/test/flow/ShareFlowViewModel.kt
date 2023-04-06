package com.xjx.kotlin.ui.activity.test.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ShareFlowViewModel : ViewModel() {
    private val _shareFlow = MutableSharedFlow<Int>()
    val sharedFlow = _shareFlow.asSharedFlow()

    fun repeat() {
        viewModelScope.launch {
            var count = 0
            repeat(100) {
                count++
                _shareFlow.emit(count)

                delay(1000)
            }
        }
    }
}