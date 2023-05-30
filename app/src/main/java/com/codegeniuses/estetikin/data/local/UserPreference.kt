package com.codegeniuses.estetikin.data.local

import android.content.Context

class UserPreference(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun getToken(): String {
        val token = preferences.getString(TOKEN, null)
        return token.toString()
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