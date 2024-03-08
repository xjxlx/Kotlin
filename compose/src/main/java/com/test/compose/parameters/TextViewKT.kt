package com.test.compose.parameters

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp

data class TextParameter @JvmOverloads constructor(

    val text: String = "",
    val onClick: () -> Unit = {},
    val style: TextStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
    val modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
)

class ProviderText : PreviewParameterProvider<TextParameter> {
    private val parameter = TextParameter()
    override val values: Sequence<TextParameter>
        get() {
            return listOf(parameter).asSequence()
        }
    override val count: Int
        get() = 1
}