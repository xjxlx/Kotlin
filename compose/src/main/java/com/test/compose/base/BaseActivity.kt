package com.test.compose.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

abstract class BaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(savedInstanceState = savedInstanceState)
    }

    abstract fun initView(savedInstanceState: Bundle?)

    fun startActivity(cls: Class<out Activity>) {
        startActivity(Intent(this, cls))
    }
}