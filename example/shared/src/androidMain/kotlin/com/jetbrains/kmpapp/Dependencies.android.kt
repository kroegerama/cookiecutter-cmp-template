package com.jetbrains.kmpapp

import androidx.core.content.pm.PackageInfoCompat
import com.jetbrains.kmpapp.model.AppVersion
import com.kroegerama.kmp.kaiteki.PlatformContext
import com.kroegerama.kmp.kaiteki.applicationContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
@BindingContainer
object PlatformBindings {
    @Provides
    fun providePlatformContext(): PlatformContext = applicationContext

    @Provides
    fun provideAppVersion(context: PlatformContext): AppVersion {
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
        return AppVersion(
            versionName = packageInfo.versionName ?: "",
            versionCode = PackageInfoCompat.getLongVersionCode(packageInfo).toString(),
            applicationId = context.packageName
        )
    }
}
