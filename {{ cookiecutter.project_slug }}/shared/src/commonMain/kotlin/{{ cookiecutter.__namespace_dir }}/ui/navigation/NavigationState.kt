package {{ cookiecutter.namespace }}.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.ViewModelStoreProvider
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreProvider
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.result.rememberResultEventBusNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun rememberNavigationState(
    startRoute: Route,
    topLevelRoutes: Set<Route>
): NavigationState {
    val topLevelRoute = rememberSerializable(
        startRoute, topLevelRoutes,
        serializer = MutableStateSerializer(Route.serializer()),
    ) {
        mutableStateOf(startRoute)
    }

    val backStacks = topLevelRoutes.associateWith { key ->
        rememberRouteNavBackStack(key)
    }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute,
            backStacks = backStacks
        )
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun rememberRouteNavBackStack(
    vararg elements: Route,
): NavBackStack<Route> {
    val configuration = remember {
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(Route::class) {
                    subclassesOfSealed<Route>()
                }
            }
        }
    }
    return rememberSerializable(
        configuration = configuration,
        serializer = NavBackStackSerializer(Route.serializer()),
    ) {
        NavBackStack(*elements)
    }
}

class NavigationState(
    val startRoute: Route,
    topLevelRoute: MutableState<Route>,
    val backStacks: Map<Route, NavBackStack<Route>>
) {
    var topLevelRoute: Route by topLevelRoute
    val stacksInUse: List<Route>
        get() = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }
}

@Composable
fun NavigationState.toEntries(
    entryProvider: (Route) -> NavEntry<Route>
): SnapshotStateList<NavEntry<Route>> {
    val providers: Map<NavKey, ViewModelStoreProvider> = backStacks.mapValues { (key, _) ->
        rememberViewModelStoreProvider(key = key)
    }

    val decoratedEntries = backStacks.mapValues { (key, stack) ->
        val decorators: List<NavEntryDecorator<Route>> = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberResultEventBusNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(providers.getValue(key)),
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider
        )
    }

    return stacksInUse
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}
