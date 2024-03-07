package com.test.compose.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.test.compose.widget.Title
import com.test.compose.widget.TitleParameter

abstract class BaseTitleActivity : BaseActivity() {

    @Composable
    override fun InitView() {
        Column(Modifier.fillMaxSize()) {
            val titleContent = getTitleContent()
            Title(TitleParameter(titleContent, { onBackPressed() }))
            InitTitleView()
        }
    }

    abstract fun getTitleContent(): String

    @Preview
    @Composable
    abstract fun InitTitleView()
}