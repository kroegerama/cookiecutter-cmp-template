package com.jetbrains.kmpapp

import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.useExistingImageAsPlaceholder
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.jetbrains.kmpapp.api.ImageClient
import com.jetbrains.kmpapp.core.AppInitializer
import com.jetbrains.kmpapp.core.PlatformConfig
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient

@ContributesIntoSet(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class ImageLoaderInitializer(
    private val platformConfig: PlatformConfig,
    @ImageClient private val imageClient: HttpClient
) : AppInitializer {

    @OptIn(ExperimentalCoilApi::class)
    override fun init() {
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .crossfade(700)
                .useExistingImageAsPlaceholder(true)
                .components {
                    add(KtorNetworkFetcherFactory(httpClient = { imageClient }))
                }
                .logger(if (platformConfig.isDebug) DebugLogger() else null)
                .build()
        }
    }
}
