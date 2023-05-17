package com.tilicho.chatify

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataStore(private val context: Context) {
    companion object {
        private const val USER_DATASTORE = "datastore"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)
        val EMAIL = stringPreferencesKey("EMAIL")
        val UID = stringPreferencesKey("UID")
    }

    suspend fun saveToDataStore(email: String, password: String) {
        context.dataStore.edit {
            it[EMAIL] = email
            it[UID] = password
        }
    }

    fun getUid(): Flow<String?> {
        return context.dataStore.data.map {
            it[UID]
        }
    }

    fun getEmail(): Flow<String?> {
        return context.dataStore.data.map {
            it[EMAIL]
        }
    }
}