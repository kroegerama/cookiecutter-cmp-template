package com.jetbrains.kmpapp.ui.navigation

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import com.jetbrains.kmpapp.ui.screens.ImageScreen
import com.jetbrains.kmpapp.ui.screens.StartScreen
import com.kroegerama.kmp.kaiteki.compose.navigation.ScaffoldSceneDecorator
import kotlinx.serialization.Serializable

@Serializable
sealed interface RootNavKey : Route {

    @Serializable
    data object Start : RootNavKey

    @Serializable
    data object Details : RootNavKey
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun rootEntryProvider(
    navigator: Navigator
): (Route) -> NavEntry<Route> = entryProvider {
    entry<RootNavKey.Start>(
        metadata = ListDetailSceneStrategy.listPane() + ScaffoldSceneDecorator.topAppBar {
            CenterAlignedTopAppBar({ Text("Hello World") })
        }
    ) {
        StartScreen(
            navigator = navigator
        )
    }
    entry<RootNavKey.Details>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        ImageScreen(
            navigator = navigator
        )
    }
}
