package com.jetbrains.kmpapp

import com.kroegerama.kmp.kaiteki.PlatformContext
import okio.Path
import okio.Path.Companion.toOkioPath

actual fun imageCacheDirectory(context: PlatformContext): Path = context.cacheDir.resolve("image_cache").toOkioPath()
