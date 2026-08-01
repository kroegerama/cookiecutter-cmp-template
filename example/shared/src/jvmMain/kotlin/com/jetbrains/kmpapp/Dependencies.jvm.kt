package com.jetbrains.kmpapp

import com.jetbrains.kmpapp.model.AppVersion
import com.kroegerama.kmp.kaiteki.PlatformContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
@BindingContainer
object PlatformBindings {
    @Provides
    fun providePlatformContext(): PlatformContext = PlatformContext.INSTANCE

    @Provides
    fun provideAppVersion(): AppVersion {
        return AppVersion(
            versionName = "todo",
            versionCode = "todo",
            applicationId = "todo"
        )
    }
}
