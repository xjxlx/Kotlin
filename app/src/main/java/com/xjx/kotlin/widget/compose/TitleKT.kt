package com.xjx.kotlin.widget.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
    Row(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .height(50.dp)
    ) {
        // back icon
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .width(50.dp)
                .clickable {
                    ToastUtil.show("1111")
                }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
        ) {
            Icon(painter = painterResource(id = R.mipmap.icon_base_title_back), contentDescription = "", tint = Color.White)
        }

        // title
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1F),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "compose", style = TextStyle(fontSize = 18.sp, color = Color.White)
            )
        }

        // right title
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ediText",
                style = TextStyle(fontSize = 18.sp, color = Color.White),
                modifier = Modifier.padding(5.dp, 0.dp, 5.dp, 0.dp)
            )
        }
    }
}