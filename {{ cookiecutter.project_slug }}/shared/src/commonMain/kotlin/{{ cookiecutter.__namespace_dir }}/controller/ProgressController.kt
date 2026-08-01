package {{ cookiecutter.namespace }}.controller

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

@SingleIn(AppScope::class)
@Inject
@Stable
class ProgressController {
    private val mutex = Mutex()
    private val active = mutableListOf<LoadingState>()
    private val current = MutableStateFlow<LoadingState?>(null)

    // undelayed; blocks input while the dialog is still in its show delay
    val busy: Flow<Boolean> = current.map { it != null }.distinctUntilChanged()

    // anti-flicker: appears only after ShowDelay, then stays visible for at least MinShowTime
    val loading: Flow<LoadingState?> = flow {
        var shownAt: TimeSource.Monotonic.ValueTimeMark? = null
        emitAll(
            current.transformLatest { state ->
                if (state == null) {
                    shownAt?.let { mark ->
                        val remaining = MinShowTime - mark.elapsedNow()
                        if (remaining.isPositive()) delay(remaining)
                    }
                    shownAt = null
                    emit(null)
                } else {
                    if (shownAt == null) {
                        delay(ShowDelay)
                        shownAt = TimeSource.Monotonic.markNow()
                    }
                    emit(state)
                }
            }
        )
    }

    suspend fun <T> loadWithProgress(
        label: String? = null,
        block: suspend () -> T
    ): T {
        val state = LoadingState(
            label = label
        )
        mutex.withLock {
            active += state
            current.value = state
        }
        return try {
            block()
        } finally {
            mutex.withLock {
                active -= state
                current.value = active.lastOrNull()
            }
        }
    }

    @Immutable
    data class LoadingState(
        val label: String? = null
    )

    private companion object {
        val ShowDelay = 150.milliseconds
        val MinShowTime = 500.milliseconds
    }
}
