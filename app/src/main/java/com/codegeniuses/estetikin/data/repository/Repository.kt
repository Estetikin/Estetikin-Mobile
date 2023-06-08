package com.codegeniuses.estetikin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.codegeniuses.estetikin.data.local.UserPreference
import com.codegeniuses.estetikin.data.remote.ApiService
import com.codegeniuses.estetikin.model.response.*
import com.codegeniuses.estetikin.model.result.Result
import com.codegeniuses.estetikin.model.result.Result.*

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

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Loading)
        try {
            val response = apiService.login(email, password)
            if (response.error) {
                emit(Error(response.message))
            } else {
                emit(Success(response))
            }
        } catch (e: Exception) {
            emit(Error(e.message.toString()))
        }
    }

    fun getAllModuleDetail(): LiveData<Result<ModuleDetailResponse>> = liveData {
        emit(Loading)
        val token = pref.getToken()
        try {
            val response = apiService.getAllModule("Bearer $token")
            if (response.error) {
                emit(Error(response.status))
            } else {
                emit(Success(response))
            }
        } catch (e: Exception) {
            emit(Error(e.message.toString()))
        }
    }

    fun getArticles(type: String): LiveData<Result<ArticleResponse>> = liveData {
        emit(Loading)
        val token = pref.getToken()
        try {
            val response = apiService.getArticles("Bearer $token", type)
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