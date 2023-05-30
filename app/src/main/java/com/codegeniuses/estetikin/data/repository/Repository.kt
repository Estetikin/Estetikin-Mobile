package com.codegeniuses.estetikin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.codegeniuses.estetikin.data.Result
import com.codegeniuses.estetikin.data.Result.*
import com.codegeniuses.estetikin.data.local.UserPreference
import com.codegeniuses.estetikin.data.remote.ApiService
import com.codegeniuses.estetikin.model.response.GeneralResponse

class Repository(private val pref: UserPreference, private val apiService: ApiService) {

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): LiveData<Result<GeneralResponse>> =
        liveData {
            emit(Loading)
            try {
                val response = apiService.register(name, email, password, confirmPassword)
                if (response.error) {
                    emit(Error(response.message))
                } else {
                    emit(Success(response))
                }
            } catch (e: Exception) {
                emit(Error(e.message.toString()))
            }
        }


    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            preferences: UserPreference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(preferences, apiService)
            }.also { instance = it }
    }

}