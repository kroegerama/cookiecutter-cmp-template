package com.jetbrains.kmpapp.api.model

data class LocalSessionData(
    val sessionToken: String,
    val refreshToken: String
)
