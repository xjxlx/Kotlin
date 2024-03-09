package com.test.compose.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.common.base.compose.BaseTitleActivity
import com.test.compose.parameters.ProviderText
import com.test.compose.parameters.TextParameter

class ViewsMapActivity : BaseTitleActivity() {

    override fun getTitleContent(): String {
        return "ViewsMap"
    }

    @Preview
    @Composable
    override fun InitTitleView() {
        Column {
            Items(TextParameter(text = "状态栏颜色", onClick = { startActivity(StatusBarColorActivity::class.java) }))
            Items(TextParameter(text = "导航跳转", onClick = { startActivity(NavigationActivity::class.java) }))
            Items(TextParameter(text = "顶部导航栏", onClick = { startActivity(TopAppBarActivity::class.java) }))
            Items(TextParameter(text = "底部导航栏", onClick = { startActivity(BottomBarActivity::class.java) }))
//            Items(TextParameter(text = "底部导航栏2", onClick = { startActivity(BottomAppBar2Activity::class.java) }))
        }
    }

    @Composable
    fun Items(@PreviewParameter(ProviderText::class) text: TextParameter) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Blue)
            .clickable {
                text.onClick()
            }) {
            Text(
                text = text.text, style = TextStyle(color = Color.White, fontSize = 23.sp), modifier = Modifier.padding(10.dp)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}

