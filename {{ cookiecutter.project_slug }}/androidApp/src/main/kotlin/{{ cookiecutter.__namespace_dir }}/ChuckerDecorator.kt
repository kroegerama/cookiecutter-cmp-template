package {{ cookiecutter.namespace }}

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import {{ cookiecutter.namespace }}.api.HttpClientDecorator
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
