package com.jetbrains.kmpapp.core

interface AppInitializer {

    val order: Int get() = 0

    fun init()
}
