package com.jetbrains.kmpapp.controller

import androidx.datastore.preferences.core.edit
import com.jetbrains.kmpapp.api.createDataStore
import com.kroegerama.kmp.kaiteki.PlatformContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

private const val appDataStoreFileName = "appdata.preferences_pb"

@SingleIn(AppScope::class)
@Inject
class DataStore(
    context: PlatformContext
) {

    private val dataStore = createDataStore(context, appDataStoreFileName)

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        // TODO add datastore keys here
    }
}
