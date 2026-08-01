package com.jetbrains.kmpapp

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Init.initAll(BuildConfig.DEBUG)
        Init.appGraph.apiInitializer.updateClient {
            engine {
                val chuckerCollector = ChuckerCollector(
                    context = this@AndroidApp,
                    showNotification = false
                )
                val chuckerInterceptor = ChuckerInterceptor.Builder(this@AndroidApp)
                    .collector(chuckerCollector)
                    .alwaysReadResponseBody(true)
                    .build()
                addInterceptor(chuckerInterceptor)
            }
        }
    }
}
