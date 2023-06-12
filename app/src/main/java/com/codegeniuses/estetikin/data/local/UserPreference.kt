package com.codegeniuses.estetikin.data.local

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import java.util.*

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

    fun getNickname(): String? {
        return preferences.getString(NICKNAME, null)
    }

    fun saveNickname(nickname: String) {
        val edit = preferences.edit()
        edit.putString(NICKNAME, nickname)
        edit.apply()
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

    fun applyTheme() {
        val theme = getTheme()
        if (!theme.isNullOrEmpty()) {
            when (theme) {
                "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    fun setLocale(context: Context) {
        val language = getLanguage()
        if (!language.isNullOrEmpty()) {
            val locale = Locale(language)
            Locale.setDefault(locale)

            val configuration = Configuration()
            configuration.setLocale(locale)
            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        }
    }

    companion object {
        private const val PREF_LANGUAGE = "Language"
        private const val PREF_THEME = "Theme"
        private const val NICKNAME = "nickname"
        private const val TOKEN = "token"
        private const val USER_PREFERENCE = "user_preference"
    }
}