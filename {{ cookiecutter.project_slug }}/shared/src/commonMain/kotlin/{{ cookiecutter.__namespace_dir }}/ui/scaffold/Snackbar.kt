package {{ cookiecutter.namespace }}.ui.scaffold

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import com.kroegerama.kmp.kaiteki.compose.scaffold.SnackbarColors
import com.kroegerama.kmp.kaiteki.compose.scaffold.SnackbarController
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

@SingleIn(AppScope::class)
@Inject
@Stable
class AppSnackbarController : SnackbarController() {
    suspend fun show(
        message: StringResource,
        actionLabel: StringResource? = null,
        onAction: (() -> Unit)? = null,
        colors: SnackbarColors = SnackbarColors.Default
    ) = show(
        message = getString(message),
        actionLabel = actionLabel?.let { getString(it) },
        onAction = onAction,
        colors = colors
    )
}

val LocalSnackbarController: ProvidableCompositionLocal<SnackbarController> = compositionLocalOf {
    error("No SnackbarController provided.")
}
