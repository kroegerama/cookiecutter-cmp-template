package {{ cookiecutter.namespace }}

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.recalculateWindowInsets
import androidx.compose.material3.ExperimentalMaterial3ComponentOverrideApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldValue
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreProvider
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.result.rememberResultEventBusNavEntryDecorator
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.ui.NavDisplay
import {{ cookiecutter.namespace }}.ui.dialogs.LoadingDialog
import {{ cookiecutter.namespace }}.ui.icons.AppIcons
import {{ cookiecutter.namespace }}.ui.icons.Cookie
import {{ cookiecutter.namespace }}.ui.navigation.LoginNavKey
import {{ cookiecutter.namespace }}.ui.navigation.Navigator
import {{ cookiecutter.namespace }}.ui.navigation.RootNavKey
import {{ cookiecutter.namespace }}.ui.navigation.Route
import {{ cookiecutter.namespace }}.ui.navigation.loginEntryProvider
import {{ cookiecutter.namespace }}.ui.navigation.rememberNavigationState
import {{ cookiecutter.namespace }}.ui.navigation.rememberRouteNavBackStack
import {{ cookiecutter.namespace }}.ui.navigation.rememberSceneDecorator
import {{ cookiecutter.namespace }}.ui.navigation.rootEntryProvider
import {{ cookiecutter.namespace }}.ui.navigation.toEntries
import {{ cookiecutter.namespace }}.ui.scaffold.AppSnackbarHost
import {{ cookiecutter.namespace }}.ui.scaffold.LocalSharedTransitionScope
import {{ cookiecutter.namespace }}.ui.scaffold.LocalSnackbarController
import {{ cookiecutter.namespace }}.ui.theme.AppTheme
import com.kroegerama.kmp.kaiteki.compose.navigation.rememberAlertDialogSceneStrategy
import com.kroegerama.kmp.kaiteki.compose.navigation.rememberBottomSheetSceneStrategy
import com.kroegerama.kmp.kaiteki.compose.navigation.rememberScaffoldSceneDecorator
import com.kroegerama.kmp.kaiteki.compose.platform.rememberCustomTabsUriHandler
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.metroViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3ComponentOverrideApi::class)
@Composable
fun App() {
    CompositionLocalProvider(
        LocalMetroViewModelFactory provides Init.appGraph.metroViewModelFactory
    ) {
        val viewModel = metroViewModel<AppViewModel>()

        val uriHandler = rememberCustomTabsUriHandler()

        val snackbarHostState = remember { SnackbarHostState() }
        val snackbarController = Init.appGraph.snackbarController
        snackbarController.LaunchSnackbarEffect(snackbarHostState)

        val loggedIn by viewModel.loggedIn.collectAsStateWithLifecycle()
        val loadingState by viewModel.loading.collectAsStateWithLifecycle()

        val adaptiveInfo = currentWindowAdaptiveInfoV2()

        AppTheme {
            SharedTransitionLayout {
                CompositionLocalProvider(
                    LocalUriHandler provides uriHandler,
                    LocalSnackbarController provides snackbarController,
                    LocalSharedTransitionScope provides this,
                ) {
                    loggedIn?.let { isLoggedIn ->
                        AnimatedContent(
                            targetState = isLoggedIn,
                            label = "session"
                        ) { state ->
                            if (state) {
                                LoggedInContent(
                                    snackbarHostState = snackbarHostState,
                                    adaptiveInfo = adaptiveInfo
                                )
                            } else {
                                LoginContent(
                                    snackbarHostState = snackbarHostState,
                                    adaptiveInfo = adaptiveInfo
                                )
                            }
                        }
                    }
                    loadingState?.let {
                        LoadingDialog(
                            label = it.label
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun SharedTransitionScope.LoginContent(
    snackbarHostState: SnackbarHostState,
    adaptiveInfo: WindowAdaptiveInfo
) {
    val backStack = rememberRouteNavBackStack(LoginNavKey.Login)
    val viewModelStoreProvider = rememberViewModelStoreProvider(key = LoginNavKey.Login)
    val navEntries = rememberDecoratedNavEntries(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberResultEventBusNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(viewModelStoreProvider)
        ),
        entryProvider = loginEntryProvider(backStack)
    )

    NavScaffold(
        snackbarHostState = snackbarHostState,
        adaptiveInfo = adaptiveInfo,
        entries = navEntries,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() }
    )
}

@Composable
private fun SharedTransitionScope.LoggedInContent(
    snackbarHostState: SnackbarHostState,
    adaptiveInfo: WindowAdaptiveInfo
) {
    val navigationState = rememberNavigationState(
        startRoute = RootNavKey.Start,
        topLevelRoutes = setOf(RootNavKey.Start)
    )
    val navigator = remember { Navigator(navigationState) }

    val entryProvider = rootEntryProvider(navigator)
    val entries = navigationState.toEntries(entryProvider)

    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState(
        initialValue = NavigationSuiteScaffoldValue.Hidden
    )
    val scaffoldSceneDecorator = rememberScaffoldSceneDecorator<Route>(this)
    val sceneDecorator = rememberSceneDecorator<Route>(
        onShowNavigationSuite = { show ->
            if (show) {
                navigationSuiteScaffoldState.show()
            } else {
                navigationSuiteScaffoldState.hide()
            }
        }
    )

    NavigationSuiteScaffold(
        navigationItemVerticalArrangement = Arrangement.Center,
        navigationSuiteType = NavigationSuiteScaffoldDefaults.navigationSuiteType(adaptiveInfo),
        state = navigationSuiteScaffoldState,
        navigationItems = {
            NavigationItems.entries.forEach { nav ->
                NavigationSuiteItem(
                    selected = navigationState.topLevelRoute == nav.route,
                    onClick = {
                        navigator.navigate(nav.route)
                    },
                    icon = { Icon(nav.icon, nav.label()) },
                    label = { Text(nav.label()) }
                )
            }
        }
    ) {
        NavScaffold(
            snackbarHostState = snackbarHostState,
            adaptiveInfo = adaptiveInfo,
            entries = entries,
            onBack = navigator::goBack,
            sceneDecoratorStrategies = listOf(scaffoldSceneDecorator, sceneDecorator)
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun SharedTransitionScope.NavScaffold(
    snackbarHostState: SnackbarHostState,
    adaptiveInfo: WindowAdaptiveInfo,
    entries: List<NavEntry<Route>>,
    onBack: () -> Unit,
    sceneDecoratorStrategies: List<SceneDecoratorStrategy<Route>> = emptyList()
) {
    val directive = calculatePaneScaffoldDirective(adaptiveInfo)
    val bottomSheetSceneStrategy = rememberBottomSheetSceneStrategy<Route>()
    val alertDialogSceneStrategy = rememberAlertDialogSceneStrategy<Route>()
    val listDetailSceneStrategy = rememberListDetailSceneStrategy<Route>(
        directive = directive,
    )

    Scaffold(
        snackbarHost = {
            AppSnackbarHost(snackbarHostState)
        },
        contentWindowInsets = WindowInsets(),
        modifier = Modifier
            .fillMaxSize()
            .recalculateWindowInsets()
    ) { innerPadding ->
        NavDisplay(
            entries = entries,
            sceneStrategies = listOf(
                bottomSheetSceneStrategy,
                alertDialogSceneStrategy,
                listDetailSceneStrategy
            ),
            sceneDecoratorStrategies = sceneDecoratorStrategies,
            sharedTransitionScope = this@NavScaffold,
            onBack = onBack,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        )
    }
}

enum class NavigationItems(
    val route: Route,
    val icon: ImageVector,
    val label: @Composable () -> String
) {
    Start(
        route = RootNavKey.Start,
        icon = AppIcons.Cookie,
        label = { "Start" }
    )
}
