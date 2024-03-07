package com.test.compose.ui.views

import androidx.compose.runtime.Composable
import com.test.compose.base.BaseTitleActivity

class ViewsMapActivity : BaseTitleActivity() {

    override fun getTitleContent(): String {
        return "底部导航栏"
    }

    @Composable
    override fun InitTitleView() {
    }
}