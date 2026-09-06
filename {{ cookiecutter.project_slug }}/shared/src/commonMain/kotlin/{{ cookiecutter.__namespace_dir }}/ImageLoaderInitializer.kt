package {{ cookiecutter.namespace }}

import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.useExistingImageAsPlaceholder
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import {{ cookiecutter.namespace }}.api.ImageClient
import {{ cookiecutter.namespace }}.core.AppInitializer
import {{ cookiecutter.namespace }}.core.PlatformConfig
import com.kroegerama.kmp.kaiteki.PlatformContext
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
    private val platformContext: PlatformContext,
    @ImageClient private val imageClient: HttpClient
) : AppInitializer {

    @OptIn(ExperimentalCoilApi::class)
    override fun init() {
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .crossfade(700)
                .useExistingImageAsPlaceholder(true)
                .memoryCache {
                    MemoryCache.Builder()
                        .maxSizePercent(context, 0.25)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(imageCacheDirectory(platformContext))
                        .maxSizePercent(0.25)
                        .build()
                }
                .components {
                    add(KtorNetworkFetcherFactory(httpClient = { imageClient }))
                }
                .logger(if (platformConfig.isDebug) DebugLogger() else null)
                .build()
        }
    }
}
