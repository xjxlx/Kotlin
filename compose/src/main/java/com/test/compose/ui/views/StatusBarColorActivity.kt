package com.test.compose.ui.views

import androidx.compose.runtime.Composable
import com.android.common.base.compose.BaseTitleActivity

class StatusBarColorActivity : BaseTitleActivity() {

    override fun getTitleContent(): String {
        return "状态栏的颜色"
    }

    @Composable
    override fun InitTitleView() {
    }
}
