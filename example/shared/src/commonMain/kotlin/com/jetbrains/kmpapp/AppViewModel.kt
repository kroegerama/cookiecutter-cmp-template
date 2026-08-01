package com.jetbrains.kmpapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.kmpapp.api.SessionStore
import com.jetbrains.kmpapp.controller.ProgressController
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class AppViewModel(
    progressController: ProgressController,
    sessionStore: SessionStore
) : ViewModel() {
    val loading = progressController.loading

    // null = session state not yet loaded; the splash screen stays visible until it resolves
    val loggedIn: StateFlow<Boolean?> = sessionStore.loggedInFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )
}
