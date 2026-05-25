package com.example.outfitbase.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.outfitbase.domain.model.AppSettings
import com.example.outfitbase.domain.model.Language
import com.example.outfitbase.domain.model.ThemeMode
import com.example.outfitbase.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AppSettingsViewModel(
    settingsRepository: SettingsRepository
) : ViewModel() {
    val settings: StateFlow<AppSettings> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppSettings(
                themeMode = ThemeMode.SYSTEM,
                language = Language.ROMANIAN
            )
        )
}

class AppSettingsViewModelFactory(
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppSettingsViewModel(settingsRepository) as T
    }
}
