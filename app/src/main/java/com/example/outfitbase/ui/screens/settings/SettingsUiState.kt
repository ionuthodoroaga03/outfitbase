package com.example.outfitbase.ui.screens.settings

import com.example.outfitbase.domain.model.Language
import com.example.outfitbase.domain.model.ThemeMode

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: Language = Language.ROMANIAN
)
