package com.example.outfitbase.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.outfitbase.OutfitBaseApplication
import com.example.outfitbase.domain.model.Language
import com.example.outfitbase.domain.model.ThemeMode

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val application = LocalContext.current.applicationContext as OutfitBaseApplication
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(application.appContainer.settingsRepository)
    )
    val uiState by viewModel.uiState.collectAsState()

    SettingsContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onThemeModeSelected = viewModel::updateThemeMode,
        onLanguageSelected = viewModel::updateLanguage
    )
}

@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    onNavigateBack: () -> Unit,
    onThemeModeSelected: (ThemeMode) -> Unit,
    onLanguageSelected: (Language) -> Unit
) {
    val text = SettingsText.from(uiState.language)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onNavigateBack) {
            Text(text.back)
        }
        Text(
            text = text.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        SettingsSectionTitle(text.theme)
        ThemeMode.entries.forEach { themeMode ->
            SettingsOption(
                label = text.themeLabel(themeMode),
                selected = uiState.themeMode == themeMode,
                onClick = { onThemeModeSelected(themeMode) }
            )
        }
        HorizontalDivider()
        SettingsSectionTitle(text.language)
        Language.entries.forEach { language ->
            SettingsOption(
                label = text.languageLabel(language),
                selected = uiState.language == language,
                onClick = { onLanguageSelected(language) }
            )
        }
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SettingsOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        TextButton(onClick = onClick) {
            Text(label)
        }
    }
}

private data class SettingsText(
    val title: String,
    val back: String,
    val theme: String,
    val language: String,
    val light: String,
    val dark: String,
    val system: String,
    val romanian: String,
    val english: String
) {
    fun themeLabel(themeMode: ThemeMode): String {
        return when (themeMode) {
            ThemeMode.LIGHT -> light
            ThemeMode.DARK -> dark
            ThemeMode.SYSTEM -> system
        }
    }

    fun languageLabel(language: Language): String {
        return when (language) {
            Language.ROMANIAN -> romanian
            Language.ENGLISH -> english
        }
    }

    companion object {
        fun from(language: Language): SettingsText {
            return when (language) {
                Language.ROMANIAN -> SettingsText(
                    title = "Setari",
                    back = "Inapoi",
                    theme = "Tema",
                    language = "Limba",
                    light = "Luminoasa",
                    dark = "Intunecata",
                    system = "Sistem",
                    romanian = "Romana",
                    english = "Engleza"
                )
                Language.ENGLISH -> SettingsText(
                    title = "Settings",
                    back = "Back",
                    theme = "Theme",
                    language = "Language",
                    light = "Light",
                    dark = "Dark",
                    system = "System",
                    romanian = "Romanian",
                    english = "English"
                )
            }
        }
    }
}
