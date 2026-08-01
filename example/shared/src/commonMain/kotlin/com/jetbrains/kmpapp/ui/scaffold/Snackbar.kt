package com.jetbrains.kmpapp.ui.scaffold

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

@Immutable
data class SnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    val containerColor: Color = Color.DarkGray,
    val contentColor: Color = Color.White
) : androidx.compose.material3.SnackbarVisuals

@SingleIn(AppScope::class)
@Inject
@Stable
class SnackbarController {
    private val channel: Channel<SnackbarVisuals> = Channel(Channel.CONFLATED)
    private var successColor: Color = Color.DarkGray
    private var onSuccessColor: Color = Color.White

    suspend fun showSuccess(messageRes: StringResource) =
        showSuccess(getString(messageRes))

    fun showSuccess(message: String) {
        channel.trySend(
            SnackbarVisuals(
                message = message,
                containerColor = successColor,
                contentColor = onSuccessColor
            )
        )
    }

    @Composable
    fun LaunchSnackbarEffect(snackbarHostState: SnackbarHostState) {
        val colorScheme = MaterialTheme.colorScheme
        LaunchedEffect(this, snackbarHostState) {
            successColor = colorScheme.inverseSurface
            onSuccessColor = colorScheme.inverseOnSurface
            channel.receiveAsFlow().collectLatest { visuals ->
                snackbarHostState.showSnackbar(visuals)
            }
        }
    }
}

@Composable
fun AppSnackbarHost(snackbarHostState: SnackbarHostState) {
    SnackbarHost(
        hostState = snackbarHostState
    ) { data ->
        when (val visuals = data.visuals) {
            is SnackbarVisuals -> Snackbar(
                snackbarData = data,
                containerColor = visuals.containerColor,
                contentColor = visuals.contentColor,
                modifier = Modifier.safeDrawingPadding()
            )

            else -> Snackbar(
                snackbarData = data,
                modifier = Modifier.safeDrawingPadding()
            )
        }
    }
}

val LocalSnackbarController: ProvidableCompositionLocal<SnackbarController> = compositionLocalOf {
    error("No SnackbarController provided.")
}
