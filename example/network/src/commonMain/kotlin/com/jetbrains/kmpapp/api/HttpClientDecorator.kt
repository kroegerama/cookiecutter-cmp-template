package com.jetbrains.kmpapp.api

import com.kroegerama.openapi.kmp.gen.companion.PlatformHttpClientEngineConfig
import io.ktor.client.HttpClientConfig

fun interface HttpClientDecorator {
    fun HttpClientConfig<PlatformHttpClientEngineConfig>.decorate()
}

fun HttpClientConfig<PlatformHttpClientEngineConfig>.applyDecorators(decorators: List<HttpClientDecorator>) {
    decorators.forEach { with(it) { decorate() } }
}
