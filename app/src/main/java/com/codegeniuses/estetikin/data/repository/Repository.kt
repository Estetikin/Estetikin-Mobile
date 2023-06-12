package com.codegeniuses.estetikin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.codegeniuses.estetikin.data.local.UserPreference
import com.codegeniuses.estetikin.data.remote.ApiService
import com.codegeniuses.estetikin.model.response.GeneralResponse
import com.codegeniuses.estetikin.model.response.album.AlbumResponse
import com.codegeniuses.estetikin.model.response.article.ArticleResponse
import com.codegeniuses.estetikin.model.response.login.LoginResponse
import com.codegeniuses.estetikin.model.response.module.ModuleResponse
import com.codegeniuses.estetikin.model.response.profile.GetProfileResponse
import com.codegeniuses.estetikin.model.response.profile.ProfileResponse
import com.codegeniuses.estetikin.model.response.upload.UploadResponse
import com.codegeniuses.estetikin.model.result.Result
import com.codegeniuses.estetikin.model.result.Result.*
import okhttp3.MultipartBody

class Repository(private val pref: UserPreference, private val apiService: ApiService) {

    fun register(
        name: String,
        nickname: String,
        email: String,
        password: String,
        confirmPassword: String
    ): LiveData<Result<GeneralResponse>> =
        liveData {
            emit(Loading)
            try {
                val response = apiService.register(name, nickname, email, password, confirmPassword)
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

    fun getNickname(){
//        TODO("I Don't Know what to get here, where should i get it?")
    }

    fun getAllModule(): LiveData<Result<ModuleResponse>> = liveData {
        emit(Loading)
        val token = pref.getToken()
        try {
            val response = apiService.getAllModule("Bearer $token")
            if (response.error) {
                emit(Error(response.message))
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

    fun getHistoryAlbum(): LiveData<Result<AlbumResponse>> = liveData {
        emit(Loading)
        val token = pref.getToken()
        try {
            val response = apiService.getHistoryAlbum("Bearer $token")
            if (response.error) {
                emit(Error(response.status))
            } else {
                emit(Success(response))
            }
        } catch (e: Exception) {
            emit(Error(e.message.toString()))
        }
    }

    fun uploadImage(
        imageMultipart: MultipartBody.Part,
        class1: Int,
        class2: Int,
        class3: Int,
        class4: Int
    ): LiveData<Result<UploadResponse>> = liveData {
        emit(Loading)
        val token = pref.getToken()
        try {
            val response = apiService.uploadImage(
                "Bearer $token",
                imageMultipart,
                class1,
                class2,
                class3,
                class4
            )
            if (response.error) {
                emit(Error(response.message))
            } else {
                emit(Success(response))
            }
        } catch (e: Exception) {
            emit(Error(e.message.toString()))
        }
    }

    fun getProfilePicture(): LiveData<Result<GetProfileResponse>> = liveData {
        emit(Loading)
        val token = pref.getToken()
        try {
            val response = apiService.getProfilePicture("Bearer $token")
            if (response.error) {
                emit(Error(response.status))
            } else {
                emit(Success(response))
            }
        } catch (e: Exception) {
            emit(Error(e.message.toString()))
        }
    }
    fun uploadProfile(
        imageMultipart: MultipartBody.Part
    ): LiveData<Result<ProfileResponse>> = liveData{
        emit(Loading)
        val token = pref.getToken()
        try {
            val response = apiService.uploadProfilePicture(
                "Bearer $token",
                imageMultipart,
            )
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