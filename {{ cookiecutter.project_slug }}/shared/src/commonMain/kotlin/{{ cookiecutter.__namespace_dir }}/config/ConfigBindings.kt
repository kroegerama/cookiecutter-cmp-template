package {{ cookiecutter.namespace }}.config

import {{ cookiecutter.namespace }}.api.model.ApiConfig
import {{ cookiecutter.namespace }}.api.pokeapi.Api
import {{ cookiecutter.namespace }}.model.AppVersion
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.http.Url

@ContributesTo(AppScope::class)
@BindingContainer
object ConfigBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun provideApiConfig(
        appVersion: AppVersion
    ): ApiConfig = ApiConfig(
        baseUrl = Api.baseUrl,
        versionName = appVersion.versionName,
        versionCode = appVersion.versionCode,
        applicationId = appVersion.applicationId
    )
}
