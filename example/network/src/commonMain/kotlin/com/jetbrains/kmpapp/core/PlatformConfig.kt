package com.jetbrains.kmpapp.core

import com.jetbrains.kmpapp.api.HttpClientDecorator

class PlatformConfig(
    val isDebug: Boolean,
    val httpClientDecorators: List<HttpClientDecorator> = emptyList()
)
