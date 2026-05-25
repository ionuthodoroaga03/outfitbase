package com.outfitbase.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.outfitbase.domain.repository.OrderRepository
import com.outfitbase.domain.repository.SettingsRepository
import com.outfitbase.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    userProfileRepository: UserProfileRepository,
    settingsRepository: SettingsRepository,
    orderRepository: OrderRepository
) : ViewModel() {
    val uiState: StateFlow<ProfileUiState> = combine(
        userProfileRepository.getUserProfile(),
        settingsRepository.getSettings(),
        orderRepository.getOrders()
    ) { userProfile, settings, orders ->
        ProfileUiState(
            userProfile = userProfile,
            language = settings.language,
            orders = orders
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState()
    )
}

class ProfileViewModelFactory(
    private val userProfileRepository: UserProfileRepository,
    private val settingsRepository: SettingsRepository,
    private val orderRepository: OrderRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(userProfileRepository, settingsRepository, orderRepository) as T
    }
}
