package {{ cookiecutter.namespace }}.api

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.core.okio.OkioStorage
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesSerializer
import androidx.datastore.preferences.core.emptyPreferences
import com.kroegerama.kmp.kaiteki.PlatformContext
import com.kroegerama.kmp.kaiteki.dataDirectory
import okio.FileSystem

actual fun createDataStore(context: PlatformContext, fileName: String): DataStore<Preferences> = PreferenceDataStoreFactory.create(
    storage = OkioStorage(
        fileSystem = FileSystem.SYSTEM,
        serializer = PreferencesSerializer,
    ) {
        context.dataDirectory / "datastore" / fileName
    },
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() }
)
