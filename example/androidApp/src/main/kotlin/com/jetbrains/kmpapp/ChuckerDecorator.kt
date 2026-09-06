package com.jetbrains.kmpapp

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jetbrains.kmpapp.api.HttpClientDecorator
import com.kroegerama.openapi.kmp.gen.companion.PlatformHttpClientEngineConfig
import io.ktor.client.HttpClientConfig

class ChuckerDecorator(
    context: Context
) : HttpClientDecorator {

    private val chuckerInterceptor = ChuckerInterceptor.Builder(context)
        .collector(
            ChuckerCollector(
                context = context,
                showNotification = false
            )
        )
        .alwaysReadResponseBody(true)
        .build()

    override fun HttpClientConfig<PlatformHttpClientEngineConfig>.decorate() {
        engine {
            addInterceptor(chuckerInterceptor)
        }
    }
}
