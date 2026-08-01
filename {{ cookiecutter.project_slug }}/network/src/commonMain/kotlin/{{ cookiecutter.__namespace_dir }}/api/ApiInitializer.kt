package {{ cookiecutter.namespace }}.api

import arrow.core.getOrElse
import co.touchlab.kermit.Logger
import {{ cookiecutter.namespace }}.api.model.ApiConfig
import {{ cookiecutter.namespace }}.api.pokeapi.Api
import com.kroegerama.kmp.kaiteki.Initializer
import com.kroegerama.openapi.kmp.gen.companion.PlatformHttpClientEngineConfig
import com.kroegerama.openapi.kmp.gen.companion.createPlatformHttpClient
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequest

@ContributesIntoSet(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class ApiInitializer(
    private val apiConfig: ApiConfig,
    private val sessionStore: SessionStore
) : Initializer {

    override fun init(isDebug: Boolean) {
        Api.baseUrl = apiConfig.baseUrl
        updateClient()
    }

    fun updateClient(
        localDecorator: HttpClientConfig<PlatformHttpClientEngineConfig>.() -> Unit = {}
    ) {
        Api.updateClient(
            withLogging = true,
            createHttpClient = { apiDecorator ->
                createPlatformHttpClient {
                    apiDecorator()
                    localDecorator()
                }
            }
        ) {
            defaultRequest {
                headers {
                    append("app-version", apiConfig.versionName)
                    append("app-version-code", apiConfig.versionCode)
                    append("app-id", apiConfig.applicationId)
                    append("device-os-release", osRelease)
                }
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        sessionStore.getBearer()
                    }
                    refreshTokens {
                        refreshSession(oldTokens)
                    }
                }
            }
            HttpResponseValidator {
                handleResponseExceptionWithRequest { cause, request ->
                    if (cause is ClientRequestException) {
                        handleClientRequestException(cause, request)
                    }
                }
            }
        }
    }

    private suspend fun handleClientRequestException(cause: ClientRequestException, request: HttpRequest) {
        val response = cause.response
        Logger.d(cause) { "handleClientRequestException" }
    }

    private suspend fun RefreshTokensParams.refreshSession(oldTokens: BearerTokens?): BearerTokens? {
        val refreshToken = oldTokens?.refreshToken ?: return null
        val sessionData = AuthRepository.refreshSession(
            refreshToken = refreshToken
        ) {
            markAsRefreshTokenRequest()
        }.getOrElse {
            Logger.d(it) { "refresh error" }
            sessionStore.clearBearer()
            return null
        }.data
        return sessionStore.updateBearer(sessionData)
    }

}
