package com.jetbrains.kmpapp.controller

import com.jetbrains.kmpapp.api.SessionStore
import com.jetbrains.kmpapp.core.AppObserver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@ContributesIntoSet(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class LogoutHandler(
    private val dataStore: DataStore,
    private val sessionStore: SessionStore
) : AppObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun start() {
        scope.launch {
            sessionStore.loggedInFlow.collect { loggedIn ->
                if (!loggedIn) {
                    dataStore.clear()
                }
            }
        }
    }
}
