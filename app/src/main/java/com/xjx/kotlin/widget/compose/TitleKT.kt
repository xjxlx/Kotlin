package com.xjx.kotlin.widget.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.common.R
import com.android.common.utils.ToastUtil

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Title() {
    TopAppBar(title = {
        Surface(color = Color.Cyan) {
            Row {
                // back icon
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
//                    .background(Color.Blue)
                        .width(30.dp)
                        .clickable {
                            ToastUtil.show("111")
                        }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start
                ) {
                    Image(painter = painterResource(id = R.mipmap.icon_base_title_back), contentDescription = "back")
                }

                // title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxHeight()
//                    .background(Color.Red)
                ) {
                    Text(
                        text = "compose", modifier = Modifier, color = Color.Red
                    )
                }

                // right title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(text = "right", minLines = 1, style = TextStyle(fontSize = 18.sp, color = Color.White))
                }
            }
        }
    })
}