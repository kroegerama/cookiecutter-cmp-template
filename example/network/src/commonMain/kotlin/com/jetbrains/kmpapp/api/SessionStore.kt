package com.jetbrains.kmpapp.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import co.touchlab.kermit.Logger
import com.jetbrains.kmpapp.api.model.LocalSessionData
import com.jetbrains.kmpapp.api.pokeapi.Api
import com.kroegerama.kmp.kaiteki.PlatformContext
import com.kroegerama.kmp.kaiteki.datastore.flow
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal const val sessionStoreFileName = "session-store.preferences_pb"
expect fun createDataStore(context: PlatformContext, fileName: String): DataStore<Preferences>

@SingleIn(AppScope::class)
@Inject
class SessionStore(
    context: PlatformContext
) {
    private val dataStore = createDataStore(context, sessionStoreFileName)

    val bearerFlow: Flow<BearerTokens?> = dataStore.flow { preferences ->
        val session = preferences[KEY_SESSION] ?: return@flow null
        val refresh = preferences[KEY_REFRESH] ?: return@flow null
        BearerTokens(
            accessToken = session,
            refreshToken = refresh
        )
    }

    val loggedInFlow: Flow<Boolean> = bearerFlow.map { it != null }.distinctUntilChanged()

    suspend fun getBearer(): BearerTokens? = bearerFlow.first()

    suspend fun updateBearer(sessionData: LocalSessionData): BearerTokens {
        Logger.d { sessionData.toString() }
        dataStore.edit { preferences ->
            preferences[KEY_SESSION] = sessionData.sessionToken
            preferences[KEY_REFRESH] = sessionData.refreshToken
        }
        return BearerTokens(
            accessToken = sessionData.sessionToken,
            refreshToken = sessionData.refreshToken
        )
    }

    suspend fun clearBearer() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_SESSION)
            preferences.remove(KEY_REFRESH)
        }
        Api.client.authProvider<BearerAuthProvider>()?.clearToken()
    }

    companion object {
        private val KEY_SESSION = stringPreferencesKey("session")
        private val KEY_REFRESH = stringPreferencesKey("refresh")
    }
}
