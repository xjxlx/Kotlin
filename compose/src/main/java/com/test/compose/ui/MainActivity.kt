package com.test.compose.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.common.base.compose.BaseTitleActivity
import com.android.common.utils.ToastUtil
import com.test.compose.ui.function.FunctionMapActivity
import com.test.compose.ui.views.ViewsMapActivity
import com.test.compose.widget.Items
import com.test.compose.widget.ItemsParameter

class MainActivity : BaseTitleActivity() {

    override fun getTitleContent(): String {
        return "Compose集合"
    }

    @Composable
    override fun InitTitleView() {
        LinearLayout()
    }

    @Preview
    @Composable
    fun LinearLayout() {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Items(items = ItemsParameter("3333",{ToastUtil.show("2222")}))
            
            Items(items = ItemsParameter(title = "Views") {
                startActivity(ViewsMapActivity::class.java)
            })
            Items(items = ItemsParameter(title = "123", onClick = { ToastUtil.show("111") }))

            Items(items = ItemsParameter(title = "Functions") {
                startActivity(FunctionMapActivity::class.java)
            })
        }
    }
}
