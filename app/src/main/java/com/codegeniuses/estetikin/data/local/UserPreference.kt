package com.codegeniuses.estetikin.data.local

import android.content.Context

class UserPreference(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun getPref() {

    }

    fun savePref() {
    }

    fun clearPref() {
        preferences.edit().clear().apply()
    }

    companion object {

    }
}