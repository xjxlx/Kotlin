package com.test.compose.ui.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.test.compose.base.BaseTitleActivity

class ViewsMapActivity : BaseTitleActivity() {

    override fun getTitleContent(): String {
        return "底部导航栏"
    }

    @Composable
    override fun InitTitleView() {
        NavigationBar {

        }
    }

    @Composable
    fun NavigationBar(navController: NavController) {
        BottomAppBar() {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Menu, contentDescription = null)
            }
            Spacer(modifier = Modifier.weight(1f, true))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Favorite")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More options")
            }
        }
    }

    @Composable
    fun BottomNavigationItem(icon: @Composable () -> Unit, label: () -> Unit, selected: Any?, onClick: () -> Unit) {
    }
}

