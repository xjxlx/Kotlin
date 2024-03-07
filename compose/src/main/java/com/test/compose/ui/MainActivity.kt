package com.test.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.compose.base.BaseActivity
import com.test.compose.ui.theme.KotlinTheme
import com.test.compose.ui.views.ViewsMapActivity
import com.test.compose.widget.Title
import com.test.compose.widget.TitleParameter

class MainActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContent {
            KotlinTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    LinearLayout()
                }
            }
        }
    }

    @Preview
    @Composable
    fun LinearLayout() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            // title
            Title(TitleParameter("Compose集合", { finish() }))
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1F)
                    .verticalScroll(rememberScrollState())
            ) {
                Items(title = "views") {
                    startActivity(ViewsMapActivity::class.java)
                }
            }
        }
    }

    @Composable
    fun Items(title: String, onclick: () -> Unit) {
        // 增加间距
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .background(Color.Blue)
                .clickable {
                    onclick()
                }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(color = Color.White, fontSize = 20.sp),
            )
        }
    }
}