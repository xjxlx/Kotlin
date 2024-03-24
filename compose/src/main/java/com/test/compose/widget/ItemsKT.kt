package com.test.compose.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun Items(@PreviewParameter(ItemsProvider::class) items: ItemsParameter) {
    // 增加间距
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .background(Color.Blue)
            .clickable {
                items.onClick
            }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = items.title,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(color = Color.White, fontSize = 20.sp),
        )
    }
}

data class ItemsParameter @JvmOverloads constructor(
    val title: String, val onClick: () -> Unit
)

class ItemsProvider : PreviewParameterProvider<ItemsParameter> {
    private val parameter = ItemsParameter("") {}
    override val values: Sequence<ItemsParameter>
        get() {
            return listOf(parameter).asSequence()
        }
    override val count: Int
        get() = 1
}
