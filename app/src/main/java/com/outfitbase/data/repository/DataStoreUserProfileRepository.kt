package com.outfitbase.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.outfitbase.domain.model.UserProfile
import com.outfitbase.domain.repository.UserProfileRepository
import com.outfitbase.util.InputSanitizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreUserProfileRepository(
    context: Context
) : UserProfileRepository {
    private val dataStore = context.appPreferencesDataStore

    override fun getUserProfile(): Flow<UserProfile> {
        return dataStore.data.map { preferences ->
            UserProfile(
                userName = preferences[userNameKey] ?: "Ionut Hodoroaga",
                email = preferences[emailKey] ?: "ionut@example.com",
                phone = preferences[phoneKey] ?: "0712345678"
            )
        }
    }

    override suspend fun updateUserProfile(userProfile: UserProfile) {
        dataStore.edit { preferences ->
            preferences[userNameKey] = InputSanitizer.sanitizeStoredText(userProfile.userName)
            preferences[emailKey] = InputSanitizer.sanitizeStoredText(userProfile.email, 80)
            preferences[phoneKey] = InputSanitizer.sanitizePhone(userProfile.phone)
        }
    }

    private companion object {
        val userNameKey = stringPreferencesKey("user_name")
        val emailKey = stringPreferencesKey("email")
        val phoneKey = stringPreferencesKey("phone")
    }
}
