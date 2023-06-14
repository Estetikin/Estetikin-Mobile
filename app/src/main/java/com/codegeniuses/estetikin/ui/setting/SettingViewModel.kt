package com.codegeniuses.estetikin.ui.setting

import androidx.lifecycle.ViewModel
import com.codegeniuses.estetikin.data.repository.Repository
import okhttp3.MultipartBody


class SettingViewModel (private val repo: Repository): ViewModel() {
    fun uploadProfileImage(
        imageMultipartBody: MultipartBody.Part
    ) = repo.uploadProfile(imageMultipartBody)

    fun getProfileImage() = repo.getProfilePicture()
}