package com.codegeniuses.estetikin.data.local

import android.content.Context

class UserPreference(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun getToken(): String? {
        return preferences.getString(TOKEN, null)
    }

    fun saveToken(token: String) {
        val edit = preferences.edit()
        edit.putString(TOKEN, token)
        edit.apply()
    }

    fun clearToken() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val TOKEN = "token"
    }
}