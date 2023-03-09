package com.xjx.kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * @author : 流星
 * @CreateDate: 2023/3/9-10:37
 * @Description:
 */
class TestViewModel : ViewModel() {

    fun getTest() {
        viewModelScope.launch {

        }
    }
}