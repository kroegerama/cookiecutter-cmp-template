package com.jetbrains.kmpapp

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import com.jetbrains.kmpapp.core.AppInitializer
import com.jetbrains.kmpapp.core.PlatformConfig
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@ContributesIntoSet(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class LoggingInitializer(
    private val platformConfig: PlatformConfig
) : AppInitializer {

    override val order = -100

    override fun init() {
        Logger.setMinSeverity(
            if (platformConfig.isDebug) Severity.Verbose else Severity.Warn
        )
    }
}
