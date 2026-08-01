package {{ cookiecutter.namespace }}

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.compose.useExistingImageAsPlaceholder
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import {{ cookiecutter.namespace }}.api.pokeapi.Api
import com.kroegerama.kmp.kaiteki.Initializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraph

object Init {
    val appGraph by lazy { createGraph<AppGraph>() }

    fun initAll(isDebug: Boolean) {
        initKermit(isDebug)
        appGraph.initializers.forEach { it.init(isDebug) }
    }

    private fun initKermit(isDebug: Boolean) {
        Logger.setMinSeverity(
            if (isDebug) Severity.Verbose else Severity.Warn
        )
    }
}

@ContributesIntoSet(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class ImageLoaderInitializer : Initializer {
    override fun init(isDebug: Boolean) {
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .crossfade(700)
                .useExistingImageAsPlaceholder(true)
                .components {
                    add(KtorNetworkFetcherFactory(httpClient = { Api.client }))
                }
                .logger(if (isDebug) DebugLogger() else null)
                .build()
        }
    }
}
