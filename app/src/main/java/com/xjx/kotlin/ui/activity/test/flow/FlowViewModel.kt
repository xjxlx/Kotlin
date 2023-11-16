package com.xjx.kotlin.ui.activity.test.flow

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

class FlowViewModel : ViewModel() {

    var count = 10
    val bean = Bean()

    //    val mStateFlow = MutableStateFlow(Bean())
    val mStateFlow = MutableSharedFlow<Bean>()
}

data class Bean(var name: String = "张三", var age: Int = 18)