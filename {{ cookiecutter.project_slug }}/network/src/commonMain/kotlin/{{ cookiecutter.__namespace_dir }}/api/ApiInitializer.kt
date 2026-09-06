package {{ cookiecutter.namespace }}.api

import arrow.core.getOrElse
import co.touchlab.kermit.Logger
import {{ cookiecutter.namespace }}.api.model.ApiConfig
import {{ cookiecutter.namespace }}.api.pokeapi.Api
import {{ cookiecutter.namespace }}.core.AppInitializer
import {{ cookiecutter.namespace }}.core.PlatformConfig
import com.kroegerama.openapi.kmp.gen.companion.createPlatformHttpClient
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
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
    private val sessionStore: SessionStore,
    private val platformConfig: PlatformConfig
) : AppInitializer {

    override fun init() {
        Api.baseUrl = apiConfig.baseUrl
        updateClient()
    }

    private fun updateClient() {
        Api.updateClient(
            withLogging = platformConfig.isDebug,
            createHttpClient = { apiDecorator ->
                createPlatformHttpClient {
                    apiDecorator()
                    applyDecorators(platformConfig.httpClientDecorators)
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
