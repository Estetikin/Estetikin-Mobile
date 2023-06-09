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

    fun getLanguage(): String? {
        return preferences.getString(PREF_LANGUAGE, null)
    }

    fun setLanguage(language: String?) {
        preferences.edit().putString(PREF_LANGUAGE, language).apply()
    }

    fun getTheme(): String? {
        return preferences.getString(PREF_THEME, null)
    }

    fun setTheme(theme: String?) {
        preferences.edit().putString(PREF_THEME, theme).apply()
    }


    companion object {
        private val PREF_LANGUAGE = "Language"
        private val PREF_THEME = "Theme"
        private const val TOKEN = "token"
        private const val USER_PREFERENCE = "user_preference"
    }
}