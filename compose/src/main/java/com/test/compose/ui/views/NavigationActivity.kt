package com.test.compose.ui.views

import androidx.compose.runtime.Composable
import com.test.compose.base.BaseTitleActivity

class NavigationActivity : BaseTitleActivity() {
    override fun getTitleContent(): String {
        return "Navigation"
    }

    @Composable
    override fun InitTitleView() {
    }
}