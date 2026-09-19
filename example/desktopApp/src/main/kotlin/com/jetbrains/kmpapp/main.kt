package com.jetbrains.kmpapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kroegerama.kmp.kaiteki.PlatformContext

fun main() {
    PlatformContext.initialize("com.jetbrains.kmpapp")
    Init.initAll(true)
    System.setProperty("apple.awt.application.appearance", "system")

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "CMP App",
        ) {
            App()
        }
    }
}
