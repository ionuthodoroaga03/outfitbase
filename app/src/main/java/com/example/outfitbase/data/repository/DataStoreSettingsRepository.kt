package com.example.outfitbase.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.outfitbase.domain.model.AppSettings
import com.example.outfitbase.domain.model.Language
import com.example.outfitbase.domain.model.ThemeMode
import com.example.outfitbase.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreSettingsRepository(
    context: Context
) : SettingsRepository {
    private val dataStore = context.appPreferencesDataStore

    override fun getSettings(): Flow<AppSettings> {
        return dataStore.data.map { preferences ->
            AppSettings(
                themeMode = preferences[themeModeKey]?.toThemeMode() ?: ThemeMode.SYSTEM,
                language = preferences[languageKey]?.toLanguage() ?: Language.ROMANIAN
            )
        }
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[themeModeKey] = themeMode.name
        }
    }

    override suspend fun updateLanguage(language: Language) {
        dataStore.edit { preferences ->
            preferences[languageKey] = language.name
        }
    }

    private fun String.toThemeMode(): ThemeMode {
        return ThemeMode.entries.firstOrNull { themeMode -> themeMode.name == this } ?: ThemeMode.SYSTEM
    }

    private fun String.toLanguage(): Language {
        return Language.entries.firstOrNull { language -> language.name == this } ?: Language.ROMANIAN
    }

    private companion object {
        val themeModeKey = stringPreferencesKey("theme_mode")
        val languageKey = stringPreferencesKey("language")
    }
}
