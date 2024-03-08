package com.test.compose.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.test.compose.base.BaseTitleActivity

class BottomBarActivity : BaseTitleActivity() {
    override fun getTitleContent(): String {
        return "BottomBar"
    }

    @Composable
    override fun InitTitleView() {
        BottomBar()
    }

    @Composable
    @Preview
    fun BottomBar() {

        Scaffold(
            bottomBar = {
                BottomAppBar(containerColor = Color.Red, contentColor = Color.Blue, actions = {
                    IconButton(
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxSize(),
                        onClick = { /* do something */ },
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Localized description")
                    }
                    IconButton(
                        onClick = { /* do something */ }, modifier = Modifier
                            .weight(1F)
                            .fillMaxSize()
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Localized description",
                        )
                    }
                    IconButton(modifier = Modifier
                        .weight(1F)
                        .fillMaxSize(), onClick = { /* do something */ }) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Localized description",
                        )
                    }
                    IconButton(modifier = Modifier
                        .weight(1F)
                        .fillMaxSize(), onClick = { /* do something */ }) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Localized description",
                        )
                    }
                })
            },
        ) { innerPadding ->
            Text(
                modifier = Modifier.padding(innerPadding), text = " "
            )
        }
    }
}