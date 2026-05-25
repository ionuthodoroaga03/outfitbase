package com.outfitbase.data.repository

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.appPreferencesDataStore by preferencesDataStore(name = "outfitbase_preferences")
