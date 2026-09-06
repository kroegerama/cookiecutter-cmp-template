package com.jetbrains.kmpapp

import android.app.Application
import com.jetbrains.kmpapp.core.PlatformConfig

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Init.initAll(
            PlatformConfig(
                isDebug = BuildConfig.DEBUG,
                httpClientDecorators = listOf(ChuckerDecorator(this))
            )
        )
    }
}
