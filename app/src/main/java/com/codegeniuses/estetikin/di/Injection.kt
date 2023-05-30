package com.codegeniuses.estetikin.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.codegeniuses.estetikin.data.local.UserPreference
import com.codegeniuses.estetikin.data.remote.ApiConfig
import com.codegeniuses.estetikin.data.repository.Repository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_preference")

object Injection {
    fun provideRepository(context: Context): Repository {
        val preferences = UserPreference(context)
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(preferences,apiService)
    }

}