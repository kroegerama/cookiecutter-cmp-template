package com.jetbrains.kmpapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import com.jetbrains.kmpapp.api.SessionStore
import com.jetbrains.kmpapp.api.model.LocalSessionData
import com.jetbrains.kmpapp.controller.ProgressController
import com.jetbrains.kmpapp.ui.navigation.Route
import com.jetbrains.kmpapp.ui.theme.AppTheme
import com.jetbrains.kmpapp.ui.theme.dimensions
import com.kroegerama.kmp.kaiteki.compose.components.ButtonMedium
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LoginScreen(backStack: NavBackStack<Route>) {
    val viewModel = metroViewModel<LoginScreenViewModel>()

    val actions = LoginScreenActions(
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        onLogin = viewModel::performLogin
    )

    LoginScreenContent(
        actions = actions
    )
}

private data class LoginScreenActions(
    val onBack: () -> Unit = {},
    val onLogin: () -> Unit = {}
)

@Composable
private fun LoginScreenContent(
    actions: LoginScreenActions
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .safeDrawingPadding()
                .padding(MaterialTheme.dimensions.medium)
        ) {
            Text(
                text = "Please log in",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(MaterialTheme.dimensions.small)
            )
            ButtonMedium(
                onClick = dropUnlessResumed { actions.onLogin() },
                text = "Login"
            )
        }
    }
}

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class LoginScreenViewModel(
    private val sessionStore: SessionStore,
    private val progressController: ProgressController
) : ViewModel() {

    fun performLogin() {
        viewModelScope.launch {
            progressController.loadWithProgress {
                // TODO replace with a real login API call
                delay(1000.milliseconds)
                sessionStore.updateBearer(
                    LocalSessionData(
                        sessionToken = "demo-session-token",
                        refreshToken = "demo-refresh-token"
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreenContent(
            actions = LoginScreenActions()
        )
    }
}
