package com.android.githubuserapp.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.android.githubuserapp.utility.Constants.MY_PREFERENCE
import com.android.githubuserapp.utility.Constants.MY_THEME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.myDataStore: DataStore<Preferences> by preferencesDataStore(name = MY_PREFERENCE)

class SettingRepository(context: Context) {

    private val dataStore: DataStore<Preferences> = context.myDataStore
    private val keyTheme = booleanPreferencesKey(MY_THEME)

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[keyTheme] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[keyTheme] = isDarkModeActive
        }
    }

}