package {{ cookiecutter.namespace }}.api

import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import androidx.datastore.preferences.core.emptyPreferences
import co.touchlab.kermit.Logger
import com.kroegerama.kmp.kaiteki.PlatformContext
import java.io.File

actual fun createDataStore(context: PlatformContext, fileName: String): DataStore<Preferences> = PreferenceDataStoreFactory.create(
    storage = FileStorage(
        serializer = PreferencesFileSerializer,
    ) {
        val tmpDir = File(System.getProperty("java.io.tmpdir"))
        tmpDir.resolve("datastore/$fileName").also {
            Logger.d { "data store> $it" }
        }
    },
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() }
)
