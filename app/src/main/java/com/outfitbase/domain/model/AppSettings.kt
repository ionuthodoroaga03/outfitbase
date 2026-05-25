package com.outfitbase.domain.model

data class AppSettings(
    val themeMode: ThemeMode,
    val language: Language
)

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

enum class Language(val code: String) {
    ROMANIAN("ro"),
    ENGLISH("en")
}
