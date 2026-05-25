package com.example.outfitbase.ui.screens.profile

import com.example.outfitbase.domain.model.Language
import com.example.outfitbase.domain.model.Order
import com.example.outfitbase.domain.model.UserProfile

data class ProfileUiState(
    val userProfile: UserProfile = UserProfile(
        userName = "Ionut Hodoroaga",
        email = "ionut@example.com",
        phone = "0712345678"
    ),
    val language: Language = Language.ROMANIAN,
    val orders: List<Order> = emptyList()
)
