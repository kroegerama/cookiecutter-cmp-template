package com.jetbrains.kmpapp

import com.kroegerama.kmp.kaiteki.PlatformContext
import com.kroegerama.kmp.kaiteki.cacheDirectory
import okio.Path

fun imageCacheDirectory(context: PlatformContext): Path = context.cacheDirectory / "image_cache"
