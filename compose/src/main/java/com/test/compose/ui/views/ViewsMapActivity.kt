package com.test.compose.ui.views

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.test.compose.base.BaseTitleActivity

class ViewsMapActivity : BaseTitleActivity() {

    override fun getTitleContent(): String {
        return "底部导航栏"
    }

    @Preview
    @Composable
    override fun InitTitleView() {
        Text(text = "11")
    }
}

