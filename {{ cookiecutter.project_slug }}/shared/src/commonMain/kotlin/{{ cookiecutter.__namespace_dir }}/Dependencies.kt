package {{ cookiecutter.namespace }}

import androidx.lifecycle.ViewModel
import {{ cookiecutter.namespace }}.core.AppInitializer
import {{ cookiecutter.namespace }}.core.AppObserver
import {{ cookiecutter.namespace }}.core.PlatformConfig
import {{ cookiecutter.namespace }}.ui.scaffold.AppSnackbarController
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import kotlin.reflect.KClass

@DependencyGraph(AppScope::class)
interface AppGraph : ViewModelGraph {
    val initializers: Set<AppInitializer>
    val observers: Set<AppObserver>
    val snackbarController: AppSnackbarController

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides platformConfig: PlatformConfig): AppGraph
    }
}

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class AppViewModelFactory(
    override val viewModelProviders: Map<KClass<out ViewModel>, () -> ViewModel>,
    override val assistedFactoryProviders: Map<KClass<out ViewModel>, () -> ViewModelAssistedFactory>,
    override val manualAssistedFactoryProviders: Map<KClass<out ManualViewModelAssistedFactory>, () -> ManualViewModelAssistedFactory>,
) : MetroViewModelFactory()
