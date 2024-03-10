package com.test.compose.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Icon
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
import androidx.constraintlayout.compose.ConstraintLayout
import com.android.common.base.compose.BaseTitleActivity

class ExpandableListViewActivity : BaseTitleActivity() {

    override fun getTitleContent(): String {
        return "ExpandableListView"
    }

    @Preview
    @Composable
    override fun InitTitleView() {
        ExpandableListView()
    }

    @Composable
    fun ExpandableListView() {
        /**
         * 整体结构使用Column来控制上下层
         * 1：父类层级，使用ConstraintLayout控制
         * 2：子类层级，使用Row来控制
         */
        Column {
            // 父类的布局
            ConstraintLayout(
                modifier = Modifier
                    .width(120.dp)
                    .wrapContentHeight()
                    .background(color = Color.Red, shape = RoundedCornerShape(8.dp))
            ) {
                val (icon, text) = createRefs()
                Icon(imageVector = Icons.Default.MailOutline,
                    contentDescription = "",
                    modifier = Modifier
                        .wrapContentWidth()
                        .constrainAs(icon) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(15.dp, 0.dp, 5.dp, 0.dp))
                Text(text = "S1",
                    style = TextStyle(fontSize = 18.sp, color = Color.White),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp, 5.dp, 10.dp, 5.dp)
                        .constrainAs(text) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })
            }

            // 2:子类的布局,此处应该是个循环
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(20.dp, 10.dp, 0.dp, 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.Red, shape = RoundedCornerShape(8.dp))
                        .width(5.dp)
                        .height(30.dp)

                )
                Text(text = "第一个月", textAlign = TextAlign.Center, modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp))
            }
        }
    }
}
