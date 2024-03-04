package com.xjx.kotlin.ui.activity.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.android.common.utils.ToastUtil
import com.xjx.kotlin.ui.activity.compose.ui.theme.KotlinTheme

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
        Column(
            Modifier
                .background(Color.White)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            // title
            TopAppBar(modifier = Modifier.background(Color.Black), title = {
                Row {
                    Icon(
                        painter = painterResource(id = com.android.common.R.mipmap.icon_base_title_back),
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentWidth()
                            .clickable { ToastUtil.show("111") },
                        contentDescription = "返回"
                    )
                }
            })
        }
    }
}