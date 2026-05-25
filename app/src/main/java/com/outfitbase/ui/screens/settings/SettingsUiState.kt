package com.outfitbase.ui.screens.settings

import com.outfitbase.domain.model.Language
import com.outfitbase.domain.model.ThemeMode

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: Language = Language.ROMANIAN
)
