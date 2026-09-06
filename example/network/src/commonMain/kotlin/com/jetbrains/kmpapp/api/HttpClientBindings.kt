package com.jetbrains.kmpapp.api

import com.jetbrains.kmpapp.core.PlatformConfig
import com.kroegerama.openapi.kmp.gen.companion.createPlatformHttpClient
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.Qualifier
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient

@Qualifier
annotation class ImageClient

@ContributesTo(AppScope::class)
@BindingContainer
object HttpClientBindings {
    @Provides
    @SingleIn(AppScope::class)
    @ImageClient
    fun provideImageClient(platformConfig: PlatformConfig): HttpClient = createPlatformHttpClient {
        applyDecorators(platformConfig.httpClientDecorators)
    }
}
