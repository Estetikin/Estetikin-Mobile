package com.codegeniuses.estetikin.ui.confirmPage

import androidx.lifecycle.ViewModel
import com.codegeniuses.estetikin.data.repository.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ConfirmViewModel(private val repo: Repository) : ViewModel() {

    fun uploadImage(
        imageMultipartBody: MultipartBody.Part,
        class1: RequestBody,
        class2: RequestBody,
        class3: RequestBody,
        class4: RequestBody
    ) = repo.uploadImage(imageMultipartBody, class1, class2, class3, class4)
}
