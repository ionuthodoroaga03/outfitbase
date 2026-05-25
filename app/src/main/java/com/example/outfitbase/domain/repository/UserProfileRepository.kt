package com.example.outfitbase.domain.repository

import com.example.outfitbase.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun getUserProfile(): Flow<UserProfile>

    suspend fun updateUserProfile(userProfile: UserProfile)
}
