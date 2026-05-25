package com.example.outfitbase.domain.repository

import com.example.outfitbase.domain.model.AppSettings
import com.example.outfitbase.domain.model.Language
import com.example.outfitbase.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<AppSettings>

    suspend fun updateThemeMode(themeMode: ThemeMode)

    suspend fun updateLanguage(language: Language)
}
