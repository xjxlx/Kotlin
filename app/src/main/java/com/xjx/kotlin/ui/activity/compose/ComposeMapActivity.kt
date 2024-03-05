package com.xjx.kotlin.ui.activity.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.common.utils.ToastUtil
import com.xjx.kotlin.ui.activity.compose.ui.theme.KotlinTheme
import com.xjx.kotlin.widget.compose.Title

class ComposeMapActivity : ComponentActivity() {

    //    override fun getTitleContent(): String {
//        return "Compose集合"
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinTheme {
                Surface {
                    LinearLayout()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun LinearLayout() {
        Title()
        Column(
            Modifier
                .background(Color.Yellow)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            // title
            TopAppBar(title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .background(Color.Blue)
                ) {
                    Row(
                        modifier = Modifier
                            .background(Color.Magenta)
                            .width(50.dp)
                            .fillMaxHeight()
                            .clickable { ToastUtil.show("111") },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = com.android.common.R.mipmap.icon_base_title_back),
                            contentDescription = ""
                        )
                    }
                }
            })
        }
    }
}