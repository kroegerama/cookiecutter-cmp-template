package {{ cookiecutter.namespace }}.ui.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import {{ cookiecutter.namespace }}.ui.screens.LoginScreen
import kotlinx.serialization.Serializable

sealed interface LoginNavKey : Route {

    @Serializable
    data object Login : LoginNavKey

}

fun loginEntryProvider(
    backStack: NavBackStack<Route>
): (Route) -> NavEntry<Route> = entryProvider {
    entry<LoginNavKey.Login> {
        LoginScreen(
            backStack = backStack
        )
    }
}
