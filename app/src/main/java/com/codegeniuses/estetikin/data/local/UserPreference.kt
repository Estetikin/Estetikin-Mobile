package com.codegeniuses.estetikin.data.local

import android.content.Context

class UserPreference(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)


    fun saveUserPreference(pref: String) {
        val edit = preferences.edit()
        edit.putString(USER_PREFERENCE, pref)
        edit.apply()
    }

    fun getUserPreference(): String? {
        return preferences.getString(USER_PREFERENCE, null)
    }

    fun getToken(): String? {
        return preferences.getString(TOKEN, null)
    }

    fun saveToken(token: String) {
        val edit = preferences.edit()
        edit.putString(TOKEN, token)
        edit.apply()
    }

    fun clearPreferences() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val TOKEN = "token"
        private const val USER_PREFERENCE = "user_preference"
    }
}