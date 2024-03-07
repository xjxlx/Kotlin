package com.test.compose.base

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.test.compose.widget.Title
import com.test.compose.widget.TitleParameter

abstract class BaseTitleActivity : BaseActivity() {

    @Composable
    override fun InitView(savedInstanceState: Bundle?) {
        Column(Modifier.fillMaxSize()) {
            val titleContent = getTitleContent()
            Title(TitleParameter(titleContent, { onBackPressed() }))
            InitTitleView()
        }
    }

    abstract fun getTitleContent(): String

    @Composable
    abstract fun InitTitleView()
}