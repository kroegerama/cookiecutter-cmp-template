package {{ cookiecutter.namespace }}.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.LocalListDetailSceneScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewModelScope
import {{ cookiecutter.namespace }}.api.SessionStore
import {{ cookiecutter.namespace }}.api.pokeapi.api.PokemonApi
import {{ cookiecutter.namespace }}.controller.ProgressController
import {{ cookiecutter.namespace }}.resources.Res
import {{ cookiecutter.namespace }}.resources.greeting
import {{ cookiecutter.namespace }}.ui.navigation.Navigator
import {{ cookiecutter.namespace }}.ui.navigation.RootNavKey
import {{ cookiecutter.namespace }}.ui.scaffold.AppSnackbarController
import {{ cookiecutter.namespace }}.ui.theme.AppTheme
import {{ cookiecutter.namespace }}.ui.theme.dimensions
import com.kroegerama.kmp.kaiteki.compose.components.ButtonMedium
import com.kroegerama.kmp.kaiteki.compose.components.ButtonSmall
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun StartScreen(
    navigator: Navigator
) {
    val viewModel = metroViewModel<StartScreenViewModel>()

    val actions = StartScreenActions(
        onNavigate = navigator::navigate,
        onProgress = viewModel::performProgress,
        onLogout = viewModel::performLogout
    )
    StartScreenContent(
        actions = actions,
        greeting = stringResource(Res.string.greeting)
    )
}

private data class StartScreenActions(
    val onNavigate: (RootNavKey) -> Unit = {},
    val onProgress: () -> Unit = {},
    val onLogout: () -> Unit = {}
)

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun StartScreenContent(
    actions: StartScreenActions,
    greeting: String
) {
    val scaffoldSceneScope = LocalListDetailSceneScope.current

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
                text = greeting,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(MaterialTheme.dimensions.small)
            )
            ButtonMedium(
                onClick = actions.onProgress,
                text = "Progress"
            )
            ButtonSmall(
                onClick = dropUnlessResumed { actions.onNavigate(RootNavKey.Details) },
                text = "Detail"
            )
            val scope = rememberCoroutineScope()
            ButtonSmall(
                onClick = dropUnlessResumed {
                    scope.launch {
                        val r = PokemonApi.pokemonList()
                        println("findPets> ${r.map { it.data }}")
                    }
                },
                text = "API"
            )
            ButtonSmall(
                onClick = dropUnlessResumed { actions.onLogout() },
                text = "Logout"
            )
            Text("scaffoldSceneScope> $scaffoldSceneScope")
        }
    }
}

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class StartScreenViewModel(
    private val progressController: ProgressController,
    private val snackbarController: AppSnackbarController,
    private val sessionStore: SessionStore
) : ViewModel() {
    fun performProgress() {
        viewModelScope.launch {
            progressController.loadWithProgress {
                delay(1000.milliseconds)
                snackbarController.show("Success!!!")
            }
        }
    }

    fun performLogout() {
        viewModelScope.launch {
            sessionStore.clearBearer()
        }
    }
}

@Preview
@Composable
private fun StartScreenPreview() {
    AppTheme {
        StartScreenContent(
            actions = StartScreenActions(),
            greeting = "Hello Preview"
        )
    }
}
