package com.test.compose.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.test.compose.ui.theme.KotlinTheme

abstract class BaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beforeInitView()

        setContent {
            KotlinTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    InitView()
                }
            }
        }
    }

    open fun beforeInitView() {}

    @Composable
    abstract fun InitView()

    fun startActivity(cls: Class<out Activity>) {
        startActivity(Intent(this, cls))
    }
}