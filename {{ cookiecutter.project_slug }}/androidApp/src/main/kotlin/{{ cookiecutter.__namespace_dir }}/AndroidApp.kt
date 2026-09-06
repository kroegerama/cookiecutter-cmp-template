package {{ cookiecutter.namespace }}

import android.app.Application
import {{ cookiecutter.namespace }}.core.PlatformConfig

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Init.initAll(
            PlatformConfig(
                isDebug = BuildConfig.DEBUG,
                httpClientDecorators = listOf(ChuckerDecorator(this))
            )
        )
    }
}
