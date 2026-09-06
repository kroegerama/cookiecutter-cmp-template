package {{ cookiecutter.namespace }}.core

import {{ cookiecutter.namespace }}.api.HttpClientDecorator

class PlatformConfig(
    val isDebug: Boolean,
    val httpClientDecorators: List<HttpClientDecorator> = emptyList()
)
