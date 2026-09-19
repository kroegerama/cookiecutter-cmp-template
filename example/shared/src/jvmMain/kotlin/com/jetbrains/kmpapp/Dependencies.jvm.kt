package com.jetbrains.kmpapp

import com.jetbrains.kmpapp.model.AppVersion
import com.kroegerama.kmp.kaiteki.PlatformContext
import com.kroegerama.kmp.kaiteki.platformContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import java.util.Properties

@ContributesTo(AppScope::class)
@BindingContainer
object PlatformBindings {
    @Provides
    fun providePlatformContext(): PlatformContext = platformContext

    @Provides
    fun provideAppVersion(): AppVersion {
        val properties = Properties()
        PlatformBindings::class.java.getResourceAsStream("/app-version.properties")?.use(properties::load)
        return AppVersion(
            versionName = properties.getProperty("versionName", ""),
            versionCode = properties.getProperty("versionCode", ""),
            applicationId = properties.getProperty("applicationId", "")
        )
    }
}
