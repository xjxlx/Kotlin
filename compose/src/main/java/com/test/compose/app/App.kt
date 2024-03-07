package com.test.compose.app

import android.app.Application
import com.android.common.app.ApplicationManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ApplicationManager.init(this)
    }
}