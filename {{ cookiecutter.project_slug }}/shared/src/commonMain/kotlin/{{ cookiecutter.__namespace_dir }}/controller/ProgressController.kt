package {{ cookiecutter.namespace }}.controller

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

@SingleIn(AppScope::class)
@Inject
@Stable
class ProgressController {
    private val mutex = Mutex()
    private val active = mutableListOf<LoadingState>()

    private val _loading = MutableStateFlow<LoadingState?>(null)
    val loading = _loading.asStateFlow()

    suspend fun <T> loadWithProgress(
        label: String? = null,
        block: suspend () -> T
    ): T {
        val state = LoadingState(
            label = label
        )
        mutex.withLock {
            active += state
            _loading.value = state
        }
        val start = TimeSource.Monotonic.markNow()
        return try {
            block().also {
                val remaining = 100.milliseconds - start.elapsedNow()
                if (remaining > 50.milliseconds) {
                    delay(remaining)
                }
            }
        } finally {
            withContext(NonCancellable) {
                mutex.withLock {
                    active -= state
                    _loading.value = active.lastOrNull()
                }
            }
        }
    }

    @Immutable
    data class LoadingState(
        val label: String? = null
    )
}
