package com.outfitbase.domain.repository

import com.outfitbase.domain.model.AppSettings
import com.outfitbase.domain.model.Language
import com.outfitbase.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<AppSettings>

    suspend fun updateThemeMode(themeMode: ThemeMode)

    suspend fun updateLanguage(language: Language)
}
