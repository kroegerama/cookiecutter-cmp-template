package {{ cookiecutter.namespace }}.controller

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

@SingleIn(AppScope::class)
@Inject
@Stable
class ProgressController {
    private val _loading = MutableStateFlow<LoadingState?>(null)
    val loading = _loading.asStateFlow()

    suspend fun <T> loadWithProgress(
        label: String? = null,
        block: suspend () -> T
    ): T {
        _loading.update {
            LoadingState(
                label = label
            )
        }
        val minDismiss = Clock.System.now().toEpochMilliseconds() + 100
        return try {
            block().also {
                val delta = minDismiss - Clock.System.now().toEpochMilliseconds()
                if (delta > 50) {
                    delay(delta.milliseconds)
                }
            }
        } finally {
            _loading.update { null }
        }
    }

    @Immutable
    data class LoadingState(
        val label: String? = null
    )
}
