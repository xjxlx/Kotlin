package com.xjx.kotlin.ui.activity.test.xc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.common.utils.LogUtil
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestVM : ViewModel() {

    fun test() {
        var count = 0

        viewModelScope.launch {
            repeat(Int.MAX_VALUE) {
                if (count >= 9) {
                    delay(100)
                    cancel()
                    LogUtil.e("xxxxx", "item:  stop !")
                } else {
                    LogUtil.e("xxxxx", "item: $it")
                    delay(1000)
                    count++
                }
            }
        }
    }
}