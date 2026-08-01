package {{ cookiecutter.namespace }}.controller

import {{ cookiecutter.namespace }}.api.SessionStore
import com.kroegerama.kmp.kaiteki.Initializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@ContributesIntoSet(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class LogoutHandler(
    private val dataStore: DataStore,
    private val sessionStore: SessionStore
) : Initializer {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var job: Job? = null

    override fun init(isDebug: Boolean) {
        job?.cancel()
        job = scope.launch {
            sessionStore.loggedInFlow.collect { loggedIn ->
                if (!loggedIn) {
                    dataStore.clear()
                }
            }
        }
    }
}
