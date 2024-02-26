package com.xjx.kotlin.ui.activity.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable

class ComposeMapActivity : ComponentActivity() {

//    override fun getTitleContent(): String {
//        return "Compose集合"
//    }
//
//    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityComposeMapBinding {
//        return ActivityComposeMapBinding.inflate(inflater, container, attachToRoot)
//    }
//
//    override fun initData(savedInstanceState: Bundle?) {
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tests()
        }
    }

    @Composable
    fun Tests() {
    }
}