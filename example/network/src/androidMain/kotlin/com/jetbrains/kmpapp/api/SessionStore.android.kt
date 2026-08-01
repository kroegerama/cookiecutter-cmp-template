package com.jetbrains.kmpapp.api

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.kroegerama.kmp.kaiteki.PlatformContext

actual fun createDataStore(context: PlatformContext, fileName: String): DataStore<Preferences> = PreferenceDataStoreFactory.create(
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() }
) {
    context.filesDir.resolve(fileName)
}
